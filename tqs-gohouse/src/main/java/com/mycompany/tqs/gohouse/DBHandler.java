package com.mycompany.tqs.gohouse;

import dbClasses.GeneralEntity;
import dbClasses.PlatformUser;
import dbClasses.Property;
import dbClasses.Room;
import dbClasses.University;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

public class DBHandler {
    
    private final EntityManager em;
    
    private final String PERSISTENCE_UNIT;
    
    private final double MIN_USERS;
    
    public DBHandler(String unit) {
        PERSISTENCE_UNIT = unit;
        MIN_USERS = 10.0;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.em = emf.createEntityManager();
    }
    
//-------------------------------------USER QUERIES-------------------------------------
    
    /**
     * if no users are found, an empty list is returned
     * @return all the users in the database
     */
    public List<PlatformUser> getAllUsers(){
        Query query = em.createQuery("select u from PlatformUser AS u");
        List<PlatformUser> users = query.getResultList();
        return users;
    }
    
    /**
     * 
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
     * 
     * @param email email of the user we want to register
     * @param name name (first and last) of the user
     * @param age age of the user
     * @param isCollegeStudent false if the user is not a college student and true otherwise
     * @param isDelegate false if the user is not a delegate of a university
     * @param univ university of the user. if the user does not have anything to do with any university, this is empty 
     * @return false if the user already exists, true otherwise
     */
    public boolean registerUser(String email, String name, LocalDate age, boolean isCollegeStudent, boolean isDelegate, University univ) {
        try {
            em.getTransaction().begin();
            em.persist(new PlatformUser(email, name, age, isCollegeStudent, isDelegate));
            em.getTransaction().commit();
        } catch (EntityExistsException ex) {
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
            if (!user.isIsCollegeStudent() || !isDelegate){
                University univ;
                try {
                    univ = (University) query.getSingleResult();                    
                } catch (NoResultException | RollbackException x){
                    //University does not exist in the database... Needs to be created
                    univ = new University(univName, univAddress);
                    em.getTransaction().begin();
                    em.persist(univ);
                    em.getTransaction().commit();
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
     * changes if a user is a student or not
     * @param id id of the user
     * @param isStudent boolean to update
     * @param univName Name of the university
     * @param univAddress Address of the university
     * @return true if success, false otherwise
     */
    public boolean changeIfStudent(long id, boolean isStudent, String univName, String univAddress){
        Query query = em.createQuery("Select u from University as u where u.name=:name");
        query.setParameter("name", univName);
        try {
            PlatformUser user = em.find(PlatformUser.class, id);
            if (!user.isIsCollegeStudent() || !isStudent){
                University univ;
                try {
                    univ = (University) query.getSingleResult();                    
                } catch (NoResultException | RollbackException x){
                    //University does not exist in the database... Needs to be created
                    univ = new University(univName, univAddress);
                    em.getTransaction().begin();
                    em.persist(univ);
                    em.getTransaction().commit();
                }
                em.getTransaction().begin();
                user.setIsCollegeStudent(isStudent);
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
     * get for the user with better user rating
     * @return platform user with the best rating
     */
    public PlatformUser getMostPopularUser(){
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
        return query.setMaxResults(n).getResultList();
    }
    
    /**
     * changes if the user is moderator or not
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
     * changes if the user is moderator or not
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
     * @param rent price per month
     * @param address address of the property
     * @param type type of the property (HOUSE or APARTMENT)
     * @param block block of the building in which the property is located 
     * @param floor floor in which the property is located
     * @param rooms list of the rooms of the property
     * @return true if the property was added with success
     */
    public boolean addNewProperty(long id, int rent, String address, String type, char block, int floor, Set<Room> rooms){
        PlatformUser owner;
        Property property;
        owner = em.find(PlatformUser.class, id);
        if (owner == null) return false;
        property = new Property(owner, rent, address, type, block, floor, rooms);
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
        if (owner == null || property == null) return false;
        if (owner.removeOwnedProperty(property)){
            em.remove(property);
            return true;
        } else {
            return false;
        }
    }
    
    public PlatformUser getOwner(long id){
        return em.find(Property.class, id).getOwner();  
    }
    
    public GeneralEntity getRenter(long id) {
        GeneralEntity entity = em.find(Property.class, id).getRenter();
        if (entity instanceof PlatformUser){
            return (PlatformUser) entity;
        } else {
            return (University) entity;
        }
    }
    
}
