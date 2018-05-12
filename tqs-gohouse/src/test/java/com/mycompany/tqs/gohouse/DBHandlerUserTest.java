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
    private University univ;
    
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
            em.persist(new PlatformUser("testemail"+(i-1)+"@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
            i--;
            em.getTransaction().commit();
            em.getTransaction().begin();
        }
        em.persist(new PlatformUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
        em.getTransaction().commit();
        em.getTransaction().begin();
        univ = new University("UA", "adress");
        em.persist(univ);
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test getting of all users;
     */
    @Test
    public void testGetAllUsers(){
        System.out.println("get all users");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        List<PlatformUser> users = instance.getAllUsers();
        assertEquals("Users count is wrong", NUMBER_OF_USERS, users.size());
    }

    /**
     * Test getting of all users;
     */
    @Test
    public void testGetSingleUser(){
        System.out.println("get single user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getSingleUser("testemail@gmail.com");
        assertNotEquals(user, null);
    }

    /**
     * Test register a new user .
     */
    @Test
    public void testRegisterUser() {
        System.out.println("register new user");
        String email = "newemail@gmail.com";
        String name = "New user";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isCollegeStudent = false;
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        boolean result = instance.registerUser(email, name, age, isCollegeStudent, isDelegate, univ);
        assertEquals(expResult, result);
    }
 
    /**
     * Test make a user delegate of an university.
     */
    @Test
    public void testMakeUserDelegate() {
        System.out.println("new delegate");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.changeDelegation(user.getId(), true, "UA", "address");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("No changes were saved", false, newuser.isIsDelegate());
    }
    
    /**
     * Test make a user student of an university.
     */
    @Test
    public void testMakeUserStudent() {
        System.out.println("new student");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.changeIfStudent(user.getId(), true, "UA", "rua");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("No changes were saved", false, newuser.isIsCollegeStudent());
    }
    
    /**
     * Test give rating to a user.
     */
    @Test
    public void testGiveUserRating() {
        System.out.println("give user rating");
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
        System.out.println("get the most popular user");
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
        System.out.println("get the 5 most popular users");
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
    
    /**
     * Test make a user moderator.
     */
    @Test
    public void testMakeChangeUserName() {
        System.out.println("new moderator");
        String email = "testemail@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getSingleUser(email);
        boolean result = instance.changeName(user.getId(), "NewName");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", "TestUser", newuser.getName());
    }
    
    /**
     * Test changing the birthday of a user.
     */
    @Test
    public void testChangeUserBirthday() {
        System.out.println("new moderator");
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
