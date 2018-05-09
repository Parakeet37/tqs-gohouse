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

public class DBHandlerTestUser {
    
    private EntityManager em;
    private int USER_RATING;
    private final String PERSISTENCE_UNIT = "tests";
    private final int NUMBER_OF_USERS = 10;
    
    public DBHandlerTestUser() {
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
        Query query = em.createQuery("DELETE FROM PlatformUser");
        query.executeUpdate();
        em.getTransaction().commit();
        em.getTransaction().begin();
        int i = NUMBER_OF_USERS;
        while (i>1){
            em.persist(new PlatformUser("testemail"+i+"@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
            i--;
        }
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
     * Test login of an existent user.
     */
    @Test
    public void testLogin() {
        System.out.println("login existant user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        boolean result = instance.login("testemail@gmail.com");
        assertEquals(expResult, result);
    }
    
    /**
     * Test login failure.
     */
    @Test
    public void testLoginFail() {
        System.out.println("login non existant user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.login("noemail@gmail.com");
        assertEquals(expResult, result);
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
        boolean result = instance.registerUser(email, name, age, isCollegeStudent, isDelegate);
        assertEquals(expResult, result);
    }
    
    /**
     * Test register an existent user.
     */
    @Test
    public void testRegisterExitentUser() {
        System.out.println("register an existent user");
        String email = "testemail@gmail.com";
        String name = "New user";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isCollegeStudent = false;
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.registerUser(email, name, age, isCollegeStudent, isDelegate);
        assertEquals(expResult, result);
    }
    
    /**
     * Test make a user moderator.
     */
    @Test
    public void testMakeUserModerator() {
        System.out.println("new moderator");
        String email = "testemail@gmail.com";
        String name = "TestUser";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isCollegeStudent = false;
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = new PlatformUser(email, name, age, isCollegeStudent, isDelegate);
        boolean result = instance.changePrivileges(email, true);
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", user.isIsModerator(), newuser.isIsModerator());
    }
    
    /**
     * Test make a non existent user a moderator.
     */
    @Test
    public void testMakeNonExistentUserModeratorFailure() {
        System.out.println("new moderator failure");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changePrivileges("newemail1@gmail.com", true);
        assertEquals(expResult, result);
    }
 
    /**
     * Test make a user delegate of an university.
     */
    @Test
    public void testMakeUserDelegate() {
        System.out.println("new delegate");
        String email = "testemail@gmail.com";
        String name = "TestUser";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isCollegeStudent = false;
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = new PlatformUser(email, name, age, isCollegeStudent, isDelegate);
        boolean result = instance.changeDelegation(email, true, "UA", "address");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", user.isIsDelegate(), newuser.isIsDelegate());
    }
    
    /**
     * Test register an existent user.
     */
    @Test
    public void testMakeNonExistentUserDelegate() {
        System.out.println("new delegate failure");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeDelegation("newemail1@gmail.com", true, "UA", "rua");
        assertEquals(expResult, result);
    }
    
    /**
     * Test make a user student of an university.
     */
    @Test
    public void testMakeUserStudent() {
        System.out.println("new student");
        String email = "testemail@gmail.com";
        String name = "TestUser";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isCollegeStudent = false;
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = new PlatformUser(email, name, age, isCollegeStudent, isDelegate);
        boolean result = instance.changeIfStudent(email, true, "UA", "rua");
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", user.isIsCollegeStudent(), newuser.isIsCollegeStudent());
    }
    
    /**
     * Test make a non existent user a student.
     */
    @Test
    public void testMakeNonExistentUserStudent() {
        System.out.println("new student failure");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeIfStudent("newemail1@gmail.com", true, "UA", "rua");
        assertEquals(expResult, result);
    }
    
    /**
     * Test give rating to a user.
     */
    @Test
    public void testGiveUserRating() {
        System.out.println("give user rating");
        String email = "testemail@gmail.com";
        String name = "TestUser";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isCollegeStudent = false;
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = new PlatformUser(email, name, age, isCollegeStudent, isDelegate);
        boolean result = instance.updateUserRating(email, 5);
        assertEquals(expResult, result);
        PlatformUser newuser = instance.getSingleUser(email);
        assertNotEquals("Users are equals... No changes were saved", user.getUserRating(), newuser.getUserRating());
    }
    
    /**
     * Test make a non existent user a student.
     */
    @Test
    public void testGiveNonExistentUserRating() {
        System.out.println("new student failure");
        String testemail = "newemail1@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.updateUserRating(testemail, USER_RATING);
        assertEquals(expResult, result);
    }
    
}
