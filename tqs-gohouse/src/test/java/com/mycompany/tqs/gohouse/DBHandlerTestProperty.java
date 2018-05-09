package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.Room;
import java.time.LocalDate;
import java.util.HashSet;
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

public class DBHandlerTestProperty {
 
    private EntityManager em;
    private final String PERSISTENCE_UNIT = "tests";
    
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
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false, false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAddNewPropertyGetOwner(){
        System.out.println("add new property and get owner");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        boolean expResult = true;
        boolean result = instance.addNewProperty(user, 100, "Street", "house", 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
    }
    
}
