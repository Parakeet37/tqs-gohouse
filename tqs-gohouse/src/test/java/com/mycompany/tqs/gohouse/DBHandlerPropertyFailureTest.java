package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.Property;
import dbClasses.Room;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBHandlerPropertyFailureTest {
    
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
        Query query = em.createQuery("DELETE FROM Property");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM PlatformUser");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM University");
        query.executeUpdate();
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
    }
 
    @Test
    public void testAddExistentProperty(){
        System.out.println("testing adition of an existent property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        boolean expResult = false;
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        boolean result = instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddPropertyWithNonExistentOwner(){
        System.out.println("testing adition of a new property having an owner that is not in the database");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.addNewProperty(0L, new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveNonExistentProperty(){
        System.out.println("testing the removal of a non existent property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        boolean expResult = false;
        boolean result = instance.removeProperty(user.getId(), 0);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemovePropertyFromNonExistentUser(){
        System.out.println("testing the removal of a property of a non existent user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        boolean expResult = false;
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
        }
        boolean result = instance.removeProperty(0, property.getId());
        assertEquals(expResult, result);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testInvalidRange(){
        System.out.println("testing the throwing of an exception when the inputs are invalid");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.getPropertiesInCostRange(100, 0);
    }
    
}
