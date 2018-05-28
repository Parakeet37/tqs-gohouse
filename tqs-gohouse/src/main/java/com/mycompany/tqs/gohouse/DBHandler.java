package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.Property;
import dbClasses.PropertyType;
import dbClasses.Room;
import dbClasses.University;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

@Singleton
public class DBHandler {
    
    private EntityManager em;
        
    private final double MIN_USERS = 10.0;
    
    private final String UNIT = "gohousedb";

    public DBHandler() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT);
        this.em = emf.createEntityManager();
    }


    public DBHandler(String unit) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(unit);
        this.em = emf.createEntityManager();
    }

    @PostConstruct
    public void init(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(UNIT);
        this.em = emf.createEntityManager();
    }
    
//-------------------------------------USER QUERIES-------------------------------------
    
    /**
     * gets a single user, given an email
     * @param email email of the user we want to search
     * @return the user, or null if no user is found
     */
    public PlatformUser getSingleUser(String email){
        PlatformUser user;
        try {
            Query query = em.createQuery("select u from PlatformUser AS u where u.email = :email");
            query.setParameter("email", email);
            user = (PlatformUser) query.getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }
        return user;
    }
    
    /**
     * registers an user in the platform
     * @param email email of the user we want to register
     * @param name name (first and last) of the user
     * @param age age of the user
     * @param isDelegate false if the user is not a delegate of a university
     * @return false if the user already exists, true otherwise
     */
    public boolean registerUser(String email, String name, LocalDate age, boolean isDelegate) {
        try {
            em.getTransaction().begin();
            em.persist(new PlatformUser(email, name, age, isDelegate));
            em.getTransaction().commit();
        } catch (RollbackException ex) {
            return false;
        }
        return true;
    }
    
    /**
     * changes if a user is delegate of a university or not

     * @param id id of the user

     * @param isDelegate boolean to update
     * @param univName name of the university
     * @param univAddress address o the university
     * @return true if success, false otherwise
     */
    public boolean changeDelegation(long id, boolean isDelegate, String univName, String univAddress) {
        Query query  = em.createQuery("Select u from University as u where u.name=:name");
        query.setParameter("name", univName);
        try {
            PlatformUser user = em.find(PlatformUser.class, id);
            if (isDelegate && !user.isIsDelegate()){
                University univ;
                try {
                    univ = (University) query.getSingleResult();                    
                } catch (NoResultException x){
                    //University does not exist in the database... Needs to be created
                    univ = new University(univName, univAddress);
                    em.getTransaction().begin();
                    em.persist(univ);
                    em.getTransaction().commit();
                } catch (RollbackException e){
                    return false;
                }
                em.getTransaction().begin();
                user.setIsDelegate(isDelegate);
                user.setUniversity(univ);
                em.getTransaction().commit();
            } else {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    
    /**
     * updates the rating of the user, making the average between the existent rating and the rating another user gives
     * @param id id of the user
     * @param rating rating to be added
     * @return true if success, false otherwise
     */
    public boolean updateUserRating(long id, int rating){
        try {
            PlatformUser user = em.find(PlatformUser.class, id);
            em.getTransaction().begin();
            if (user.getUserRating() != 0) user.setUserRating((rating+user.getUserRating())/2);
            else user.setUserRating(rating);
            user.setNVotes(user.getNVotes()+1);
            user.setWeightedRanking((user.getNVotes()/(user.getNVotes()+MIN_USERS))*user.getUserRating());
            em.getTransaction().commit();

        } catch (NullPointerException e) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    /**
     * get for the user with better user rating. 
     * @throws IndexOutOfBoundsException if no results are found
     * @return platform user with the best rating
     */
    public PlatformUser getMostPopularUser() throws IndexOutOfBoundsException{
        Query query = em.createQuery("Select u from PlatformUser as u order by u.weightedRating desc");
        return (PlatformUser) query.getResultList().get(0);
    }
    
    /**
     * get for the n most popular users
     * @param n number of users to return
     * @return list with the most popular users, ordered by user rating
     */
    public List getNMostPopularUsers(int n){
        Query query = em.createQuery("Select u from PlatformUser as u order by u.weightedRating desc");
        return (n!=0 ? query.setMaxResults(n).getResultList() : query.getResultList());
    }
    
    /**
     * changes the name of the given user
     * @param id id of the user we want to change privileges
     * @param name parameter to update
     * @return true if success, false otherwise
     */
    public boolean changeName(long id, String name) {
        try {
            PlatformUser user = em.find(PlatformUser.class, id);
            em.getTransaction().begin();
            user.setName(name);
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    
    /**
     * changes the birthday of the given user
     * @param id id of the user we want to change privileges
     * @param age parameter to update
     * @return true if success, false otherwise
     */
    public boolean changeBirthday(long id, LocalDate age) {
        try {
            PlatformUser user = em.find(PlatformUser.class, id);
            em.getTransaction().begin();
            user.setAge(age);
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }

//-------------------------------------PROPERTY QUERIES-------------------------------------
    
    /**
     * puts a property in the database and establishes the connection between the property and it's owner
     * @param id id of the owner of the property
     * @param longitude longitude in which the property is located
     * @param latitude latitude in which the property is located
     * @param address address of the property
     * @param type type of the property (HOUSE or APARTMENT)
     * @param block block of the building in which the property is located 
     * @param floor floor in which the property is located
     * @param rooms list of the rooms of the property
     * @return true if the property was added with success
     */
    public boolean addNewProperty(long id, float longitude, float latitude, String address, PropertyType type, char block, int floor, Set<Room> rooms){
        PlatformUser owner;
        Property property;
        owner = em.find(PlatformUser.class, id);
        if (owner == null) return false;
        property = new Property(owner, longitude, latitude, address, type, block, floor, rooms);
        em.getTransaction().begin();
        if (owner.addOwnedProperty(property)) {
            em.persist(property);
            em.getTransaction().commit();
        } else {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    

    /**
     * removes a property from the database
     * @param ownerID id of the owner of the property
     * @param propertyID id of the property itself
     * @return true if the property was added with success
     */
    public boolean removeProperty(long ownerID, long propertyID){
        PlatformUser owner = em.find(PlatformUser.class, ownerID);
        Property property = em.find(Property.class, propertyID);
        em.getTransaction().begin();
        if (owner == null || property == null) return false;
        if (owner.removeOwnedProperty(property)){
            em.remove(property);
            em.getTransaction().commit();
            return true;
        } else {
            em.getTransaction().commit();
            return false;
        }
    }
    
    /**
     * gets all the properties that are not occupied either by an university or a personal user
     * @return the properties. if no properties are found, the list goes empty
     */
    public List<Property> getAvailableProperties(){
        Query query = em.createQuery("select u from Property AS u where u.occupied = false");
        return query.getResultList();
    }
    
    /**
     * changes the owner of a property
     * @param currOwnerID id of the current owner of the property
     * @param newOwnerID id of the new owner of the property
     * @param propertyID id of the property
     * @return true if success. false otherwise
     */
    public boolean changeOwner(long currOwnerID, long newOwnerID, long propertyID){
        PlatformUser currOwner = em.find(PlatformUser.class, currOwnerID);
        PlatformUser newOwner = em.find(PlatformUser.class, newOwnerID);
        Property property = em.find(Property.class, propertyID);
        if (newOwner == null || currOwner == null || property == null) return false;
        em.getTransaction().begin();
        if (currOwner.removeOwnedProperty(property)){
            if (newOwner.addOwnedProperty(property)){
                property.setOwner(newOwner);
                em.getTransaction().commit();
                return true;
            } else {
                currOwner.addOwnedProperty(property);
                em.getTransaction().commit();
                return false;
            }
        } else {
            em.getTransaction().commit();
            return false;
        }
    }
    
    /**
     * changes the renter of the property
     * @param propertyID id of the property
     * @param newRenterID id of the new renter
     * @return true if success. false otherwise
     */
    public boolean rentPropertyToUser(long propertyID, long newRenterID){
        Property property = em.find(Property.class, propertyID);
        PlatformUser renter = em.find(PlatformUser.class, newRenterID);
        if (renter == null) return false;
        if (property == null) return false;
        Query query = em.createQuery("select u from Room AS u where "
                    + "u.property = :property and u.occupied = false");
        query.setParameter("property", property);
        if (query.getResultList().size() != property.getRooms().size()) return false;
        Iterator<Room> itr = property.getRooms().iterator();
        while (itr.hasNext()){
            rentRoomToUser(itr.next().getId(), renter.getId());
        }
        return true;
    }
    
    /**
     * gives to the university the availability to rent the property. the property is not occupied
     * @param propertyID id of the property
     * @param newRenterID id of the new renter
     * @return true if success. false otherwise
     */
    public boolean rentPropertyToUniversity(long propertyID, long newRenterID){
        Property property = em.find(Property.class, propertyID);
        University renter = em.find(University.class, newRenterID);
        if (renter == null) return false;
        if (property == null) return false;
        Query query = em.createQuery("select u from Room AS u where "
                    + "u.property = :property and u.occupied = false");
        query.setParameter("property", property);
        if (query.getResultList().size() != property.getRooms().size()) return false;
        Iterator<Room> itr = property.getRooms().iterator();
        while (itr.hasNext()){
            rentRoomToUniversity(itr.next().getId(), renter.getId());
        }
        return true;
    }
    
    /**
     * changes the occupation of the property
     * @param propertyID id of the property
     * @return true if success, false otherwise
     */
    public boolean removePropertyRoomOwnership(long propertyID){
        Property property = em.find(Property.class, propertyID);
        if (property == null) return false;
        if (property.isOccupied()) return false;
        Query query = em.createQuery("select u from Room AS u where "
                    + "u.university = :university and u.property = :property");
        query.setParameter("university", property.getRooms().iterator().next().getUniversity());
        query.setParameter("property", property);
        if (query.getResultList().size() != property.getRooms().size()) return false;
        Iterator<Room> itr = property.getRooms().iterator();
        while (itr.hasNext()){
            Room room = itr.next();
            if (!removeRoomOwnership(room.getId(), room.getUniversity().getId())) return false;
        }
        return true;
    }
    
    /**
     * changes the occupation of the property
     * @param propertyID id of the property
     * @return true if success, false otherwise
     */
    public boolean checkoutProperty(long propertyID){
        Property property = em.find(Property.class, propertyID);
        if (property == null) return false;
        if (!property.isOccupied()) return false;
        Query query = em.createQuery("select u from Room AS u where "
                    + "u.renter = :renter and u.property = :property");
        query.setParameter("renter", property.getRooms().iterator().next().getRenter());
        query.setParameter("property", property);
        if (query.getResultList().size() != property.getRooms().size()) return false;
        Iterator<Room> itr = property.getRooms().iterator();
        while (itr.hasNext()){
            if (!checkoutRoom(itr.next().getId())) return false;
        }
        return true;
    }
    
    /**
     * gives a rating to a property by a user. this rating is given when the user is leaving the property
     * @param id id of the property
     * @param rating rating the user gives
     * @return true if success, false otherwise
     */
    public boolean giveRatingToProperty(long id, int rating){
        try {
            Property property = em.find(Property.class, id);
            em.getTransaction().begin();
            if (property.getUserRating() != 0) property.setUserRating((rating+property.getUserRating())/2);
            else property.setUserRating(rating);
            property.setnVotes(property.getnVotes()+1);
            property.setWeightedRating((property.getnVotes()/(property.getnVotes()+MIN_USERS))*property.getUserRating());
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
    
    /**
     * called when a delegate of an university verifies if a property has conditions to receive people. each property can only be verified once
     * @param delegateID id of the delegate of the university
     * @param propertyID id of the property
     * @param rating rating the delegate gives to the property
     * @return true if success. false otherwise
     */
    public boolean verifyProperty(long delegateID, long propertyID, int rating){
        PlatformUser delegate = em.find(PlatformUser.class, delegateID);
        Property property = em.find(Property.class, propertyID);
        if (delegate == null || property == null) return false;
        if (!delegate.isIsDelegate() || property.isVerified()) return false;
        if (delegate.getId().equals(property.getOwner().getId())) return false; //a delegate cannot verify his own properties
        em.getTransaction().begin();
        if (!delegate.getUniversity().addVerifiedProperty(property)){
            em.getTransaction().commit();
            return false;
        }
        property.setVerifiedBy(delegate.getUniversity());
        property.setModeratorRating(rating);
        property.setVerified(true);
        em.getTransaction().commit();
        return true;
    }
    
    /**
     * gets all the properties that have been already verified by a delegate
     * @return the list of the properties. if there are no verified properties the list goes empty
     */
    public List getVerifiedProperties(){
        Query query = em.createQuery("Select u from Property as u where u.verified = true");
        return query.getResultList();
    }
    
    /**
     * gets all the properties that have not been verified by a delegate
     * @return the list of the properties. if there are no unverified properties the list goes empty
     */
    public List getUnverifiedProperties(){
        Query query = em.createQuery("Select u from Property as u where u.verified = false");
        return query.getResultList();
    }
    
    /**
     * gets a single property
     * @param id id of the property
     * @return the property object. if no property is found, this returns null
     */
    public Property getPropertyByID(long id){
        return em.find(Property.class, id);
    }
    
    public List getPropertiesByType(PropertyType type) {
        Query query = em.createQuery("select u from Property as u where u.type = :type");
        query.setParameter("type", type);
        return query.getResultList();
    }
    
    private float rad2deg(float rad) {
        return (float) ((rad * 180) / (Math.PI));
    }
    private float deg2rad( float deg) {
        return (float) ((deg * Math.PI) / (180));
    }
    
    /**
     * gets all the unverified properties in the range of 5 kilometers.
     * @param lat latitude of the location of the user.
     * @param lon longitude of the location of the user.
     * @param distance maximum distance the properties should be
     * @return a list of the properties in the range
     */
    public List getUnverifiedPropertiesInRange(float lat, float lon, double distance){
        float radiuslat = (float) (distance/110.574); //1 degree is approximately equal to 110.574 km
        float radiuslon = (float) ((Math.cos(deg2rad(radiuslat)))/111.320);
        Query query = em.createQuery("select u from Property as u where "
                + "(u.latitude between :minlat and :maxlat) and "
                + "(u.longitude between :minlon and :maxlon) and "
                + "u.verified = false");
        System.out.println(radiuslat + " " + radiuslon);
        query.setParameter("minlat", lat-radiuslat);
        query.setParameter("maxlat", lat+radiuslat);
        query.setParameter("minlon", lon-radiuslon);
        query.setParameter("maxlon", lon+radiuslon);
        return query.getResultList();
    }
    
//-------------------------------------ROOM QUERIES-------------------------------------
    
    /**
     * Adds a room to the platform
     * @param description Description of the room
     * @param rent Monthly cost
     * @param propertyID Id of the property the room is in
     * @return true if success, false otherwise
     */
    public boolean addRoom(String description, int rent, long propertyID){
        Property property = em.find(Property.class, propertyID);
        if (property == null) return false;
        Room room = new Room(description, rent, property);
        em.getTransaction().begin();
        if (property.addRoom(room)){
            em.persist(room);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }
    
    /**
     * Removes a room from the platform
     * @param roomID id of the room to remove
     * @return true if success, false otherwise
     */
    public boolean removeRoom(long roomID){
        Room room = em.find(Room.class, roomID);
        if (room == null) return false;
        if (room.isOccupied()) return false;
        Property property = room.getProperty();
        em.getTransaction().begin();
        if (property.removeRoom(room)){
            em.remove(room);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }
    
    /**
     * rent a room to a user
     * @param roomID id of the room to be rented
     * @param renterID id of the platform user that will rent the room
     * @return true if success, false otherwise
     */
    public boolean rentRoomToUser(long roomID, long renterID){
        Room room = em.find(Room.class, roomID);
        PlatformUser renter = em.find(PlatformUser.class, renterID);
        if (room == null || renter == null) return false;
        if (room.isOccupied()) return false;
        em.getTransaction().begin();
        if (renter.addRentedRoom(room)){
            room.setRenter(renter);
            room.setOccupied(true);
            Query query = em.createQuery("select u from Room AS u where "
                    + "u.property = :property and u.occupied = true");
            query.setParameter("property", room.getProperty());
            if (query.getResultList().size() == room.getProperty().getRooms().size()) 
                room.getProperty().setOccupied(true);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }
    
    /**
     * rent a room to a university, for it to be rented to users
     * @param roomID id of the room to be rented by the platform
     * @param renterID id of the university
     * @return true if success, false otherwise
     */
    public boolean rentRoomToUniversity(long roomID, long renterID){
        Room room = em.find(Room.class, roomID);
        University renter = em.find(University.class, renterID);
        if (room == null || renter == null) return false;
        if (room.isOccupied()) return false;
        em.getTransaction().begin();
        if (renter.addRentedRoom(room)){
            room.setUniversity(renter);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }
    
    /**
     * removes the ownership of a room by a university 
     * @param roomID id of the room
     * @param universityID id of the university the room is rented too
     * @return true if success, false otherwise
     */
    public boolean removeRoomOwnership(long roomID, long universityID){
        Room room = em.find(Room.class, roomID);
        if (room == null) return false;
        if (room.isOccupied()) return false;
        University renter = room.getUniversity();
        em.getTransaction().begin();
        if (renter.removeRentedRoom(room)){
            room.setUniversity(null);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }
    
    /**
     * checks out the room. the user that is renting the room is no more
     * @param roomID id of the room
     * @return true if success, false otherwise
     */
    public boolean checkoutRoom(long roomID){
        Room room = em.find(Room.class, roomID);
        if (room == null) return false;
        if (!room.isOccupied()) return false;
        PlatformUser renter = room.getRenter();
        em.getTransaction().begin();
        if (renter.removeRentedRoom(room)){
            room.setRenter(null);
            room.setOccupied(false);
            room.getProperty().setOccupied(false);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }
    
    /**
     * gets the 10 cheepest rooms of the platform
     * @return the list of the cheepest rooms, ordered
     */
    public List<Room> getCheaperRooms(){
        Query query = em.createQuery("Select u from Room as u where u.occupied=false order by u.rent");
        return query.setMaxResults(10).getResultList();
    }
    
    /**
     * gets the 10 most expensive rooms of the platform
     * @return the list of the most expensive rooms, ordered
     */
    public List<Room> getMostExpensiveRooms(){
        Query query = em.createQuery("Select u from Room as u where u.occupied=false order by u.rent desc");
        return query.setMaxResults(10).getResultList();
    }
    
    /**
     * gets all the rooms from any user that are inside the given cost range
     * @param minRent minimum rent value
     * @param maxRent maximum rent value
     * @return the rooms. if no rooms are found, the list goes empty
     */
    public List<Property> getRoomsInCostRange(int minRent, int maxRent) throws IllegalArgumentException {
        if (minRent >= maxRent) throw new IllegalArgumentException("the second argument should be higher than the first");
        Query query = em.createQuery("select u from Room AS u where u.rent between :minRent and :maxRent");
        query.setParameter("minRent", minRent);
        query.setParameter("maxRent", maxRent);
        return query.getResultList();
    }
    
    /**
     * gets all the rooms that are not occupied
     * @return the rooms. if no rooms are found, the list goes empty
     */
    public List<Room> getAvailableRooms(){
        Query query = em.createQuery("select u from Room AS u where u.occupied = false");
        return query.getResultList();
    }
    
    /**
     * changes the description of a room
     * @param roomID id of the room
     * @param description the new description
     * @return true if success, false otherwise
     */
    public boolean changeDescription(long roomID, String description){
        try {
            Room room = em.find(Room.class, roomID);
            em.getTransaction().begin();
            room.setDescription(description);
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    
    /**
     * changes the rent of a room
     * @param roomID id of the room
     * @param rent the new rent
     * @return true if success, false otherwise
     */
    public boolean changeRent(long roomID, int rent){
        try {
            Room room = em.find(Room.class, roomID);
            em.getTransaction().begin();
            room.setRent(rent);
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    
//-------------------------------------UNIVERSITY QUERIES-------------------------------------
    
    public boolean addUniversity(String name, String address){
        try {
            em.getTransaction().begin();
            em.persist(new University(name, address));
            em.getTransaction().commit();
        } catch (RollbackException ex) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    
    public List<University> getNMostPopularUniversities(int n){
        Query query = em.createQuery("Select u from University as u order by u.weightedRating desc");
        return (n!=0 ? query.setMaxResults(n).getResultList() : query.getResultList());    
    }
    
    public University getSingleUniversity(String name){
        University user;
        try {
            Query query = em.createQuery("select u from University AS u where u.name = :name");
            query.setParameter("name", name);
            user = (University) query.getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }
        return user;
    }
    
    public boolean giveRatingToUniversity(long universityID, int rating){
        try {
            University univ = em.find(University.class, universityID);
            em.getTransaction().begin();
            if (univ.getUserRating() != 0) univ.setUserRating((rating+univ.getUserRating())/2);
            else univ.setUserRating(rating);
            univ.setNVotes(univ.getNVotes()+1);
            univ.setWeightedRanking((univ.getNVotes()/(univ.getNVotes()+MIN_USERS))*univ.getUserRating());
            em.getTransaction().commit();

        } catch (NullPointerException e) {
            em.getTransaction().commit();
            return false;
        }
        return true;
    }
    
    public List<Room> getRoomsFromUniversity(long universityID) throws NullPointerException{
        University univ = em.find(University.class, universityID);
        if (univ == null) throw new NullPointerException("University not found");
        Query query = em.createQuery("select u from Room as u where u.university = :univ");
        query.setParameter("univ", univ);
        return query.getResultList();
    }
}