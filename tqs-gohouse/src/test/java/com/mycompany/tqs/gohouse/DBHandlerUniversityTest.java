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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBHandlerUniversityTest {
    
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
        em.persist(new University("ua", "TestUser"));
        em.persist(new University("ubi", "TestUser"));
        em.persist(new University("um", "TestUser"));
        em.persist(new University("ul", "TestUser"));
        em.persist(new University("up", "TestUser"));
        em.persist(new University("uc", "TestUser"));
        em.persist(new PlatformUser("testemail@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
        em.persist(new PlatformUser("testemail1@gmail.com", "TestUser", LocalDate.of(1997, 10, 20), false));
        em.getTransaction().commit();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetPopularUniversities(){
        System.out.println("testing getting the most popular universities");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        University uni = instance.getNMostPopularUniversities(1).get(0);
        instance.giveRatingToUniversity(uni.getId(), 4);
        assertEquals(instance.getNMostPopularUniversities(1).get(0).getId(), uni.getId());
        assertEquals(instance.getNMostPopularUniversities(10).size(), 6);
    }
    
    @Test
    public void testGetSingleUniversity(){
        System.out.println("testing getting a single university");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        University uni = instance.getSingleUniversity("ua");
        assertEquals(uni.getName(), "ua");
    }
    
    @Test
    public void testGiveRatingToUniversity(){
        System.out.println("testing giving rating to a university");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        University uni = instance.getNMostPopularUniversities(1).get(0);
        instance.giveRatingToUniversity(uni.getId(), 4);
        assertEquals(4, instance.getNMostPopularUniversities(1).get(0).getUserRating(), 0.0);
    }
    
    @Test
    public void testGetRoomsFromUniversity(){
        System.out.println("testing renting a property for a university");
        DBHandler instance = new DBHandler(PERSISTENCE_UNIT);
        PlatformUser user = (PlatformUser) instance.getNMostPopularUsers(2).get(0);
        University uni = instance.getSingleUniversity("UA");
        instance.addNewProperty(user.getId(), new Float(40), new Float(40), "Street", PropertyType.HOUSE, 'A', 1);
        Iterator<Property> itr = user.getOwnedProperties().iterator();
        Property property = new Property();
        while (itr.hasNext()){
            property = itr.next();
            break;
        }
        instance.addRoom("A nice room.", 100, property.getId());
        instance.rentProperty(property.getId(), uni.getId());
        assertEquals(instance.getRoomsFromUniversity(uni.getId()).size(), 1);
    }
    
}
