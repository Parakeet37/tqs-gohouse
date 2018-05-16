/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class DBHandlerUserTest{
  
    private EntityManager em;
    private final String PERSISTENCE_UNIT = "tests";
    private final int NUMBER_OF_USERS = 10;
    private long USER_ID_TEST;
    private long USER2_ID_TEST;
    
    public DBHandlerUserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        this.em = emf.createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM Property");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM PlatformUser");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM University");
        query.executeUpdate();
        em.getTransaction().commit();
        em.getTransaction().begin();
        int i = NUMBER_OF_USERS;
        while (i>2){
            em.persist(new PlatformUser("testemail"+(i-1)+"@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
            i--;
            em.getTransaction().commit();
            em.getTransaction().begin();
        }
        em.persist(new PlatformUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
        em.getTransaction().commit();
        em.getTransaction().begin();
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetAllUsers(){
        System.out.println("testing getting all users in the database");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        List<PlatformUser> users = instance.getNMostPopularUsers(0);
        assertEquals("Users count is wrong", NUMBER_OF_USERS, users.size());
    }

    @Test
    public void testGetSingleUser(){
        System.out.println("testing getting a single user from the database");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getSingleUser("testemail@gmail.com");
        assertNotEquals(user, null);
    }
    
    @Test
    public void testRegisterUser() {
        System.out.println("testing the registration of a new user");
        String email = "newemail@gmail.com";
        String name = "New user";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        boolean result = instance.registerUser(email, name, age, isDelegate);
        assertEquals(expResult, result);
    }
 
    @Test
    public void testMakeUserDelegate() {
        System.out.println("testing making a user a delegate");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.changeDelegation(user.getId(), true, "UA", "address");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("No changes were saved", false, newuser.isIsDelegate());
    }
    
    @Test
    public void testGiveUserRating() {
        System.out.println("testing giving a user rating");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.updateUserRating(user.getId(), 5);
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", 0, newuser.getUserRating());
        assertEquals("User rating not updated correctly", 5, newuser.getUserRating(), 0.0);
        result = instance.updateUserRating(user.getId(), 4);
        assertEquals(expResult, result);
        newuser=instance.getSingleUser(email);
        assertEquals(4.5, newuser.getUserRating(), 0.0);
    }
    
    @Test
    public void testGetMostPopularUser(){
        System.out.println("testing getting the most popular user of the platform");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser popuser = instance.getSingleUser("testemail@gmail.com");
        PlatformUser user = instance.getSingleUser("testemail2@gmail.com");
        instance.updateUserRating(popuser.getId(), 5);
        instance.updateUserRating(popuser.getId(), 5);
        instance.updateUserRating(popuser.getId(), 5);
        instance.updateUserRating(popuser.getId(), 4);
        instance.updateUserRating(user.getId(), 5);
        PlatformUser result = instance.getMostPopularUser();
        assertEquals("Users are not equal... Order by not working correctly", popuser.getEmail(), result.getEmail());
    }
    
    @Test
    public void testGetNMostPopularUsers(){
        System.out.println("testing getting the 5 most popular users of the platform");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser popuser = instance.getSingleUser("testemail@gmail.com");
        PlatformUser user = instance.getSingleUser("testemail2@gmail.com");
        PlatformUser third = instance.getSingleUser("testemail3@gmail.com");
        PlatformUser fourth = instance.getSingleUser("testemail4@gmail.com");
        PlatformUser fifth = instance.getSingleUser("testemail5@gmail.com");
        instance.updateUserRating(popuser.getId(), 5);
        instance.updateUserRating(popuser.getId(), 5);
        instance.updateUserRating(popuser.getId(), 5);
        instance.updateUserRating(popuser.getId(), 4);
        instance.updateUserRating(user.getId(), 5);
        instance.updateUserRating(user.getId(), 2);
        instance.updateUserRating(third.getId(), 5);
        instance.updateUserRating(third.getId(), 3);
        instance.updateUserRating(fourth.getId(), 5);
        instance.updateUserRating(fourth.getId(), 4);
        instance.updateUserRating(fourth.getId(), 5);
        instance.updateUserRating(fifth.getId(), 2);
        List result = instance.getNMostPopularUsers(5);
        assertEquals(result.size(), 5);
        assertEquals(popuser.getEmail(), ((PlatformUser)result.get(0)).getEmail());
    }
    
    @Test
    public void testMakeChangeUserName() {
        System.out.println("testing the change of name of a user");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.changeName(user.getId(), "NewName");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", "TestUser", newuser.getName());
    }
    
    @Test
    public void testChangeUserBirthday() {
        System.out.println("testing the chage of the birthday of a user");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.changeBirthday(user.getId(), LocalDate.of(1997, 10, 10));
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", LocalDate.of(1997, 10, 20), newuser.getAge());
    }
}
