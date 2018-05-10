/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.University;
import java.time.LocalDate;
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

public class DBHandlerUserFailuresTest {
    
    private EntityManager em;
    private int USER_RATING;
    private final String PERSISTENCE_UNIT = "tests";
    private final int NUMBER_OF_USERS = 10;
    private University univ;
    
    public DBHandlerUserFailuresTest() {
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
        query = em.createQuery("DELETE FROM University");
        query.executeUpdate();
        em.getTransaction().commit();
        em.getTransaction().begin();
        int i = NUMBER_OF_USERS;
        while (i>1){
            em.persist(new PlatformUser("testemail"+i+"@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
            i--;
            em.getTransaction().commit();
            em.getTransaction().begin();
        }
        univ = new University("UA", "adress");
        em.persist(univ);
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
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
        boolean result = instance.registerUser(email, name, age, isCollegeStudent, isDelegate, univ);
        assertEquals(expResult, result);
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
     * Test make a non existent user delegate.
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
     * Test give a non existent user rating.
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
    
    /**
     * Test change a non existent user's name.
     */
    @Test
    public void testChangeNonExistentUserName() {
        System.out.println("new student failure");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeName("newemail1@gmail.com", "NewName");
        assertEquals(expResult, result);
    }
    
    /**
     * Test change a non existent user's birthday.
     */
    @Test
    public void testChangeNonExistentUserBirthday() {
        System.out.println("new student failure");
        String testemail = "newemail1@gmail.com";
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeBirthday(testemail, LocalDate.of(1997, 10, 10));
        assertEquals(expResult, result);
    }
    
}
