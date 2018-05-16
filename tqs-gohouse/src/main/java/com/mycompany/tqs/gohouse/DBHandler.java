package com.mycompany.tqs.gohouse;

import dbClasses.GeneralEntity;
import dbClasses.PlatformUser;
import dbClasses.Property;
import dbClasses.Room;
import dbClasses.University;
import java.time.LocalDate;
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
    
    private final String UNIT = "goHouse";

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
            if (!isDelegate){
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
     * @param rent price per month
     * @param address address of the property
     * @param type type of the property (HOUSE or APARTMENT)
     * @param block block of the building in which the property is located 
     * @param floor floor in which the property is located
     * @param rooms list of the rooms of the property
     * @return true if the property was added with success
     */
    public boolean addNewProperty(long id, float longitude, float latitude, int rent, String address, String type, char block, int floor, Set<Room> rooms){
        PlatformUser owner;
        Property property;
        owner = em.find(PlatformUser.class, id);
        if (owner == null) return false;
        property = new Property(owner, longitude, latitude, rent, address, type, block, floor, rooms);
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
     * gets all the property from any user that are inside the given cost range
     * @param minRent minimum rent value
     * @param maxRent maximum rent value
     * @return the properties. if no properties are found, the list goes empty
     */
    public List<Property> getPropertiesInCostRange(int minRent, int maxRent) throws IllegalArgumentException {
        if (minRent >= maxRent) throw new IllegalArgumentException("the second argument should be higher than the first");
        Query query = em.createQuery("select u from Property AS u where u.rent between :minRent and :maxRent");
        query.setParameter("minRent", minRent);
        query.setParameter("maxRent", maxRent);
        return query.getResultList();
    }
    
    //not yet tested
    /**
     * gets all the properties that are not occupied either by an university or a personal user
     * @return the properties. if no properties are found, the list goes empty
     */
    public List<Property> getAvailableProperties(){
        Query query = em.createQuery("select u from Property AS u where u.occupied = false");
        return query.getResultList();
    }
    
    /**
     * changes the rent for a certain property
     * @param id id of the property
     * @param rent new value to be applied
     * @return true if success. false otherwise
     */
    public boolean changeRent(long id, int rent){
        try {
            Property prop = em.find(Property.class, id);
            em.getTransaction().begin();
            prop.setRent(rent);
            em.getTransaction().commit();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
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
    
    //not yet finished
    /**
     * changes the renter of the property
     * @param propertyID id of the property
     * @param newRenterID id of the new renter
     * @return true if success. false otherwise
     */
    public boolean rentProperty(long propertyID, long newRenterID){
        Property property = em.find(Property.class, propertyID);
        GeneralEntity renter;
        renter = em.find(PlatformUser.class, newRenterID);
        if (renter == null){
            renter = em.find(University.class, newRenterID);
        }
        if (renter == null || property == null) return false;
        em.getTransaction().begin();
        if (renter.addRentedProperty(property)){
            property.setOccupied(true);
            property.setRenter(renter);
            em.getTransaction().commit();
            return true;
        } else {
            return false;
        }
    }
    
    //not yet finished
    /**
     * changes the occupation of the property
     * @param propertyID id of the property
     * @return true if success, false otherwise
     */
    public boolean changePropertyToFree(long propertyID){
        Property property = em.find(Property.class, propertyID);
        if (property == null) return false;
        if (property.isOccupied()){
            em.getTransaction().begin();
            property.setOccupied(false);
            GeneralEntity renter = property.getRenter();
            renter.removeRentedProperty(property);
            property.setRenter(null);
            em.getTransaction().commit();
            return true;
        } else {
            return false;
        }
    }
    
    public Property getCheaperProperty() throws IndexOutOfBoundsException{
        Query query = em.createQuery("Select u from Property as u order by u.rent");
        return (Property) query.getResultList().get(0);
    }
    
    public Property getMostExpensiveProperty() throws IndexOutOfBoundsException{
        Query query = em.createQuery("Select u from Property as u order by u.rent desc");
        return (Property) query.getResultList().get(0);
    }
    
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
    
    public boolean verifyProperty(long moderatorID, long propertyID, int rating){
        PlatformUser moderator = em.find(PlatformUser.class, moderatorID);
        Property property = em.find(Property.class, propertyID);
        if (moderator == null || property == null) return false;
        if (!moderator.isIsDelegate() || property.isVerified()) return false;
        em.getTransaction().begin();
        property.setModeratorRating(rating);
        property.setVerified(true);
        em.getTransaction().commit();
        return true;
    }
    
    public List getVerifiedProperties(){
        Query query = em.createQuery("Select u from Property as u where u.verified = true");
        return query.getResultList();
    }
    
    public List getUnverifiedProperties(){
        Query query = em.createQuery("Select u from Property as u where u.verified = false");
        return query.getResultList();
    }
    
//-------------------------------------ROOM QUERIES-------------------------------------
    
    public boolean addRoom(String description, int rent, long propertyID){
        Property property = em.find(Property.class, propertyID);
        if (property == null){
            return false;
        }
        Room room = new Room(description, rent, property);
        em.getTransaction().begin();
        if (property.addRoom(room)){
            em.persist(room);
            em.getTransaction().commit();
            return true;
        } else {
            return false;
        }
    }
}