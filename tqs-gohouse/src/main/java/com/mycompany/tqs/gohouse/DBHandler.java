/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.University;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;

public class DBHandler {
    
    private final EntityManager em;
    
    private final String PERSISTENCE_UNIT;
    
    public DBHandler(String unit) {
        PERSISTENCE_UNIT = unit;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.em = emf.createEntityManager();
    }
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
     * @param email email of the user we want to verify
     * @return true if the user exists and false if the user does not exist
     */
    public boolean login(String email) {
        try {
            Query query = em.createQuery("SELECT u FROM PlatformUser AS u WHERE u.email = :email");
            query.setParameter("email", email);
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } 
    }
    
    /**
     * 
     * @param email email of the user we want to register
     * @param name name (first and last) of the user
     * @param age age of the user
     * @param isCollegeStudent false if the user is not a college student and true otherwise
     * @param isDelegate false if the user is not a delegate of a university
     * @return false if the user already exists, true otherwise
     */
    public boolean registerUser(String email, String name, LocalDate age, boolean isCollegeStudent, boolean isDelegate) {
        Query query = em.createQuery("SELECT u FROM PlatformUser AS u WHERE u.email= :email");
        query.setParameter("email", email);
        try {
            query.getSingleResult();
            System.out.println("found user");
        } catch (NoResultException e) {
            //there are no users with that email, we can proceed...
            try {
                em.getTransaction().begin();
                em.persist(new PlatformUser(email, name, age, isCollegeStudent, isDelegate));
                em.getTransaction().commit();
            } catch (RollbackException ex) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    /**
     * changes if the user is moderator or not
     * @param email email of the user we want to change privileges
     * @param isModerator boolean to update
     * @return true if success, false otherwise
     */
    public boolean changePrivileges(String email, boolean isModerator) {
        Query query = em.createQuery("Select u from PlatformUser as u where u.email= :email");
        query.setParameter("email", email);
        try {
            PlatformUser user = (PlatformUser) query.getSingleResult();
            em.getTransaction().begin();
            user.setIsModerator(isModerator);
            em.getTransaction().commit();
        } catch (NoResultException | RollbackException e) {
            return false;
        }
        return true;
    }
    
    /**
     * changes if a user is delegate of a university or not
     * @param email email of the user
     * @param isDelegate boolean to update
     * @param univName name of the university
     * @param univAddress address o the university
     * @return true if success, false otherwise
     */
    public boolean changeDelegation(String email, boolean isDelegate, String univName, String univAddress) {
        Query query = em.createQuery("Select u from PlatformUser as u where u.email= :email");
        query.setParameter("email", email);
        Query query_univ = em.createQuery("Select u from University as u where u.name=:name");
        query_univ.setParameter("name", univName);
        try {
            PlatformUser user = (PlatformUser) query.getSingleResult();
            if (!user.isIsCollegeStudent() || !isDelegate){
                University univ;
                try {
                    univ = (University) query_univ.getSingleResult();                    
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
        } catch (NoResultException | RollbackException e) {
            return false;
        }
        return true;
    }
    
    /**
     * changes if a user is a student or not
     * @param email email of the user
     * @param isStudent boolean to update
     * @param university University entity
     * @return true if success, false otherwise
     */
    public boolean changeIfStudent(String email, boolean isStudent, String univName, String univAddress){
        Query query = em.createQuery("Select u from PlatformUser as u where u.email= :email");
        query.setParameter("email", email);
        Query query_univ = em.createQuery("Select u from University as u where u.name=:name");
        query_univ.setParameter("name", univName);
        try {
            PlatformUser user = (PlatformUser) query.getSingleResult();
            if (!user.isIsCollegeStudent() || !isStudent){
                University univ;
                try {
                    univ = (University) query_univ.getSingleResult();                    
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
        } catch (NoResultException | RollbackException e) {
            return false;
        }
        return true;
    }
    
    /**
     * updates the rating of the user, making the average between the existent rating and the rating another user gives
     * @param email email of the user
     * @param rating rating to be added
     * @return true if success, false otherwise
     */
    public boolean updateUserRating(String email, int rating){
        Query query = em.createQuery("Select u from PlatformUser as u where u.email= :email");
        query.setParameter("email", email);
        try {
            PlatformUser user = (PlatformUser) query.getSingleResult();
            em.getTransaction().begin();
            if (user.getUserRating() != -1) user.setUserRating((rating+user.getUserRating())/2);
            else user.setUserRating(rating);
            em.getTransaction().commit();
        } catch (NoResultException | RollbackException e) {
            return false;
        }
        return true;
    }
}
