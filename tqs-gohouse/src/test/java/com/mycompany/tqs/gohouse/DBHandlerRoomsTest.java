/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbclasses.PlatformUser;
import dbclasses.Property;
import dbclasses.PropertyType;
import dbclasses.Room;
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

public class DBHandlerRoomsTest {
 
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
    public void testGetRoomsInCostRange(){
        System.out.println("testing getting the properties in a certain rent value range");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            Property property = itr.next();
            instance.addRoom("A nice room.", 100, property.getId());
            instance.addRoom("A nice room.", 200, property.getId());
            instance.addRoom("A nice room.", 300, property.getId());
            instance.addRoom("A nice room.", 150, property.getId());
            instance.addRoom("A nice room.", 90, property.getId());
            instance.addRoom("A nice room.", 201, property.getId());
            instance.addRoom("A nice room.", 250, property.getId());
        }
        assertEquals(instance.getRoomsInCostRange(100, 200).size(), 6);
        assertEquals(instance.getRoomsInCostRange(400, 500).size(), 0);
    }
    
    @Test
    public void testGetMostExpensiveRooms(){
        System.out.println("testing getting the most expensive rooms");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            Property property = itr.next();
            instance.addRoom("A nice room.", 100, property.getId());
            instance.addRoom("A nice room.", 200, property.getId());
            instance.addRoom("A nice room.", 300, property.getId());
            instance.addRoom("A nice room.", 150, property.getId());
            instance.addRoom("A nice room.", 90, property.getId());
            instance.addRoom("A nice room.", 201, property.getId());
            instance.addRoom("A nice room.", 250, property.getId());
        }
        assertEquals(instance.getMostExpensiveRooms().size(), 10);
    }
    
    @Test
    public void testGetCheapestRooms(){
        System.out.println("testing getting the cheapest rooms");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            Property property = itr.next();
            instance.addRoom("A nice room.", 100, property.getId());
            instance.addRoom("A nice room.", 200, property.getId());
            instance.addRoom("A nice room.", 300, property.getId());
            instance.addRoom("A nice room.", 150, property.getId());
            instance.addRoom("A nice room.", 90, property.getId());
            instance.addRoom("A nice room.", 201, property.getId());
            instance.addRoom("A nice room.", 250, property.getId());
        }
        assertEquals(instance.getCheaperRooms().size(), 10);
    }
    
    @Test
    public void testGetAvailableRooms(){
        System.out.println("testing getting all the available rooms");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = instance.getMostPopularUser();
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1, new HashSet<Room>());
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'B', 1, new HashSet<Room>());
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        while (itr.hasNext()){
            Property property = itr.next();
            instance.addRoom("A nice room.", 100, property.getId());
            instance.addRoom("A nice room.", 200, property.getId());
            instance.addRoom("A nice room.", 300, property.getId());
            instance.addRoom("A nice room.", 150, property.getId());
            instance.addRoom("A nice room.", 90, property.getId());
            instance.addRoom("A nice room.", 201, property.getId());
            instance.addRoom("A nice room.", 250, property.getId());
        }
        assertEquals(instance.getAvailableRooms().size(), 14);
    }
}
