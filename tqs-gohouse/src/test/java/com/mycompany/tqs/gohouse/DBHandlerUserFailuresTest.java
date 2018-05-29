/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbclasses.PlatformUser;
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
            em.persist(new PlatformUser("testemail"+i+"@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
            i--;
            em.getTransaction().commit();
            em.getTransaction().begin();
        }
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetAllUsersEmptyDB(){
        System.out.println("testing getting all users from an empty database");
        // emptying database...
        em.getTransaction().begin();
        Query query = em.createQuery("DELETE FROM PlatformUser");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM University");
        query.executeUpdate();
        em.getTransaction().commit();
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        assertEquals(instance.getNMostPopularUsers(0).size(), 0);
    }
    
    @Test
    public void testRegisterExitentUser() {
        System.out.println("testing registration of an existent user");
        String email = "testemail@gmail.com";
        String name = "New user";
        LocalDate age = LocalDate.of(1997, 10, 20);
        boolean isDelegate = false;
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.registerUser(email, name, age, isDelegate);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testMakeNonExistentUserDelegate() {
        System.out.println("testing the failure of making a non existent user delegate");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeDelegation(0, true, "UA", "rua");
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGiveNonExistentUserRating() {
        System.out.println("testing the failure of giving rating to a non existent user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.updateUserRating(0, USER_RATING);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testChangeNonExistentUserName() {
        System.out.println("testing the failure of changing the name of a non existent user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeName(0, "NewName");
        assertEquals(expResult, result);
    }
    
    @Test
    public void testChangeNonExistentUserBirthday() {
        System.out.println("testing the failure of changing the birthday of a non existent user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.changeBirthday(0, LocalDate.of(1997, 10, 10));
        assertEquals(expResult, result);
    }
    
}
