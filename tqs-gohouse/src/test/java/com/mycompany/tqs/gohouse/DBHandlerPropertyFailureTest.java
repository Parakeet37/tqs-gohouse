package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.Property;
import dbClasses.PropertyType;
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
    public void testAddExistentProperty(){
        System.out.println("testing adition of an existent property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        boolean expResult = false;
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        boolean result = instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testAddPropertyWithNonExistentOwner(){
        System.out.println("testing adition of a new property having an owner that is not in the database");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        boolean expResult = false;
        boolean result = instance.addNewProperty(0L, new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemoveNonExistentProperty(){
        System.out.println("testing the removal of a non existent property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        boolean expResult = false;
        boolean result = instance.removeProperty(user.getId(), 0);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRemovePropertyFromNonExistentUser(){
        System.out.println("testing the removal of a property of a non existent user");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        boolean expResult = false;
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
        }
        boolean result = instance.removeProperty(0, property.getId());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testChangeNonExistentOwner(){
        System.out.println("testing the changing of the owner of a property when the new owner user doesn't exist");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        boolean expResult = false;
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
        }
        boolean result = instance.changeOwner(user.getId(), 0, property.getId());
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRentPropertyWithoutAllRoomsVacant(){
        System.out.println("testing renting a property that doesn't have all the rooms vacant");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser renter = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        instance.addRoom("A nice room.", 110, property.getId());
        Iterator<Room> rooms = property.getRooms().iterator();
        while (rooms.hasNext()){
            instance.rentRoomToUser(rooms.next().getId(), renter.getId());
            break;
        }
        assertEquals(instance.rentPropertyToUser(property.getId(), renter.getId()), false);
    }
    
    @Test
    public void testCheckoutNotFullProperty(){
        System.out.println("testing checking out a property in which not all the rooms are occupied");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        instance.registerUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        PlatformUser renter = (PlatformUser) instance.getNMostPopularUsers(2).get(1);
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        instance.addRoom("A nice room.", 110, property.getId());
        instance.rentPropertyToUser(property.getId(), renter.getId());
        Iterator<Room> rooms = property.getRooms().iterator();
        while (rooms.hasNext()){
            instance.checkoutRoom(rooms.next().getId());
            break;
        }
        assertEquals(instance.checkoutProperty(property.getId()), false);
    }
    
    @Test
    public void testGiveRatingToNonExistentProperty(){
        System.out.println("testing giving rating to non existent property");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        assertEquals(instance.giveRatingToProperty(0, 5), false);
    }
    
    @Test
    public void testVerifyPropertyAlreadyVerified(){
        System.out.println("testing verification of a property that was already verified");
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
        instance.verifyProperty(delegate.getId(), property.getId(), 5);
        assertEquals(instance.verifyProperty(delegate.getId(), property.getId(), 5), false);
    }
}
