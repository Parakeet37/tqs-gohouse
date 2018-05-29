package com.mycompany.tqs.gohouse;

import dbclasses.PlatformUser;
import dbclasses.Property;
import dbclasses.PropertyType;
import dbclasses.Room;
import dbclasses.University;
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
        Query query = em.createQuery("DELETE FROM Room");
        query.executeUpdate();
        query = em.createQuery("DELETE FROM Property");
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
        boolean result = instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
        assertEquals(user.getOwnedProperties().size(), 1);
    }
    
    @Test
    public void testRemoveAProperty(){
        System.out.println("testing removing a property and check if that property is in the owner owned properties list");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        boolean expResult = true;
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
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
    public void testGetAvailableProperties(){
        System.out.println("testing getting all the available properties");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        assertEquals(instance.getAvailableProperties().size(), 2);
    }
    
    @Test
    public void testRentPropertyForUser(){
        System.out.println("testing renting a property for an user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser renter = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        boolean expResult = true;
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        assertEquals(instance.rentProperty(property.getId(), renter.getId()), true);
    }

    @Test
    public void testRentPropertyToUniversity(){
        System.out.println("testing renting a property for a university");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        instance.addUniversity("UA", "adress");
        University uni = instance.getSingleUniversity("UA");
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        assertEquals(instance.rentProperty(property.getId(), uni.getId()), true);
    }
    
    @Test
    public void testCheckoutProperty(){
        System.out.println("testing checking out a property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser renter = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        boolean expResult = true;
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        instance.rentProperty(property.getId(), renter.getId());
        assertEquals(instance.checkoutProperty(property.getId()), true);
    }
    
    @Test
    public void testRemoveUniversityOwnership(){
        System.out.println("testing removing the renting of a property by an university");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        instance.addUniversity("UA", "adress");
        University uni = instance.getSingleUniversity("UA");
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        instance.rentProperty(property.getId(), uni.getId());
        assertEquals(instance.removePropertyRoomOwnership(property.getId()), true);
    }
    
    @Test
    public void testChangeOwner(){
        System.out.println("testing changing the owner of a property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'D', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'C', 2, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'D', 2, new HashSet<Room>());
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
    
    @Test
    public void testGiveRating(){
        System.out.println("testing rate a property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = true;
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        boolean result = instance.giveRatingToProperty(property.getId(), 5);
        assertEquals(expResult, result);
        assertEquals("Property rating not updated correctly", 5, property.getUserRating(), 0.0);
        result = instance.giveRatingToProperty(property.getId(), 4);
        assertEquals(expResult, result);
        assertEquals(4.5, property.getUserRating(), 0.0);
    }
    
    @Test
    public void testVerifyProperty(){
        System.out.println("testing verification of a property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser delegate = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        instance.changeDelegation(delegate.getId(), true, "UA", "street");
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        assertEquals(instance.verifyProperty(delegate.getId(), property.getId(), 5), true);
        assertEquals(property.getModeratorRating(), 5, 0.0);
    }
    
    @Test
    public void testGetUnverifiedProperties(){
        System.out.println("testing getting all the unverified properties");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser delegate = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        instance.changeDelegation(delegate.getId(), true, "UA", "street");
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        instance.verifyProperty(delegate.getId(), property.getId(), 5);
        assertEquals(instance.getUnverifiedProperties().size(), 1);
    }
    
    @Test
    public void testGetVerifiedProperties(){
        System.out.println("testing getting all the verified properties");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser delegate = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        instance.changeDelegation(delegate.getId(), true, "UA", "street");
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        instance.verifyProperty(delegate.getId(), property.getId(), 5);
        assertEquals(instance.getVerifiedProperties().size(), 1);
    }
    
    @Test
    public void testGetPropertyByType(){
        System.out.println("testing getting properties by type (House or Apartment)");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser delegate = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        instance.changeDelegation(delegate.getId(), true, "UA", "street");
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Property property = new Property();
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            property = itr.next();
            break; //we only need one element
        }
        assertEquals(instance.getPropertiesByType(PropertyType.HOUSE).size(), 2);
    }
    
    @Test
    public void testGetClosestProperties(){
        System.out.println("testing getting the unverified properties in range");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40.5), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(43), new Float(30), "Street", PropertyType.HOUSE, 'C', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40.000005), new Float(39.999999), "Street", PropertyType.HOUSE, 'D', 1, new HashSet<Room>());
        assertEquals(instance.getUnverifiedPropertiesInRange(new Float(40), new Float (40), 10).size(), 2);
    }
}
