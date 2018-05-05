/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DBHandler {
    
    private final EntityManager em;
    
    private final String PERSISTANCE_UNIT = "gohousedb";
    
    public DBHandler() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT);
        this.em = emf.createEntityManager();
    }
    
    public PlatformUser login(String email) {
        TypedQuery<PlatformUser> query = em.createQuery(
            "SELECT u FROM PlatformUser u WHERE u.email=\"" + email + "\"", PlatformUser.class);
        if (query.getResultList().size()>0) return query.getResultList().get(0);
        else return null; 
    }
    
    public boolean registerUser(String email, String name, int age, boolean isCollegeStudent, boolean isModerator, boolean isDelegate, int universityId) {
        TypedQuery<PlatformUser> query = em.createQuery(
            "SELECT u FROM PlatformUser u WHERE u.email=\"" + email + "\"", PlatformUser.class);
        if (query.getResultList().size()>0) return false;
        em.getTransaction().begin();
        em.persist(new PlatformUser(email, name, age, isCollegeStudent, isModerator, isDelegate, universityId));
        em.getTransaction().commit();
        return true;
    }
}
