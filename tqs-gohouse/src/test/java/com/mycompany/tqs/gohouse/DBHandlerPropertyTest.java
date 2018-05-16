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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class DBHandlerPropertyTest {
 
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
    public void testAddNewPropertyGetOwner(){
        System.out.println("testing adition of a new property and check if that property is in the owner owned properties list");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        boolean expResult = true;
        boolean result = instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
        assertEquals(user.getOwnedProperties().size(), 1);
    }
    
    @Test
    public void testRemoveAProperty(){
        System.out.println("testing removing a property and check if that property is in the owner owned properties list");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        boolean expResult = true;
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'B', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        boolean result = instance.removeProperty(user.getId(), property.getId());
        assertEquals(result, expResult);
        assertEquals(user.getOwnedProperties().size(), 1);
    }
    
    @Test
    public void testGetPropertiesInCostRange(){
        System.out.println("testing getting the properties in a certain rent value range");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 150, "Street", "house", 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 200, "Street", "house", 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 230, "Street", "house", 'D', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 201, "Street", "house", 'A', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 99, "Street", "house", 'B', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 130, "Street", "house", 'C', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 300, "Street", "house", 'D', 2, new HashSet<Room>());
        assertEquals(instance.getPropertiesInCostRange(100, 200).size(), 4);
        assertEquals(instance.getPropertiesInCostRange(400, 500).size(), 0);
    }
    
    //teste para propriedades disponíveis
    
    //teste para arrendar propriedade
    
    //teste para libertar propriedade
    
    @Test
    public void testGetCheaperProperty(){
        System.out.println("testing getting the cheapest property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 150, "Street", "house", 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 200, "Street", "house", 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 230, "Street", "house", 'D', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 201, "Street", "house", 'A', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 99, "Street", "house", 'B', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 130, "Street", "house", 'C', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 300, "Street", "house", 'D', 2, new HashSet<Room>());
        assertEquals(instance.getCheaperProperty().getRent(), 99);
    }
    
    @Test
    public void testGetMostExpensiveProperty(){
        System.out.println("testing getting the most expensive property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 150, "Street", "house", 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 200, "Street", "house", 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 230, "Street", "house", 'D', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 201, "Street", "house", 'A', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 99, "Street", "house", 'B', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 130, "Street", "house", 'C', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 300, "Street", "house", 'D', 2, new HashSet<Room>());
        assertEquals(instance.getMostExpensiveProperty().getRent(), 300);
    }
    
    @Test
    public void testChangeRent(){
        System.out.println("testing changing the rent of a property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 150, "Street", "house", 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 200, "Street", "house", 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 230, "Street", "house", 'D', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 201, "Street", "house", 'A', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 99, "Street", "house", 'B', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 130, "Street", "house", 'C', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 300, "Street", "house", 'D', 2, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        int rent = property.getRent();
        assertEquals(instance.changeRent(property.getId(), 500), true);
        assertEquals(property.getRent(), 500);
    }
    
    @Test
    public void testChangeOwner(){
        System.out.println("testing changing the owner of a property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 100, "Street", "house", 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 150, "Street", "house", 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 200, "Street", "house", 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 230, "Street", "house", 'D', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 201, "Street", "house", 'A', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 99, "Street", "house", 'B', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 130, "Street", "house", 'C', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), 300, "Street", "house", 'D', 2, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        PlatformUser formerOwner = property.getOwner();
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser newOwner = instance.getSingleUser("testemail1@gmail.com");
        assertEquals(instance.changeOwner(formerOwner.getId(), newOwner.getId(), property.getId()), true);
        assertEquals(property.getOwner().getId(), newOwner.getId());
    }
}
