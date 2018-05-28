/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbClasses.GeneralEntity;
import dbClasses.PlatformUser;
import dbClasses.Property;
import dbClasses.PropertyType;
import dbClasses.Room;
import dbClasses.University;
import dbClasses.User;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;


/**
 *
 * @author rui
 */
@RunWith(Arquillian.class)
public class TryingArquillianTest {
    
    @ArquillianResource
    private URL base;
    private WebTarget target;

    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        
        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();
    
        /* Por alguma razão preciso de importar pratciamente as classes todas ..  */ 
        WebArchive res =  ShrinkWrap.create(WebArchive.class)
                .addClasses(GoHouseRESTApplication.class, GoHouseRESTProperties.class,
                        GoHouseRESTUsers.class, PlatformUser.class, GeneralEntity.class,
                        DBHandler.class, PropertyType.class, Property.class, University.class,
                        Room.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(files);
    
        return res; 
        
    }
    
    
    /* Aqui será o base path para a api */
    /* Para vários paths diferentes convém criar váruias classes diferentes */
    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        //target = client.target(base).path("converterApi/converter");
        target = client.target(URI.create(new URL(base, "REST/users").toExternalForm()));
        target.register(User.class);
    }
    
    /* Fazer os testes aqui */
    
    @Test
    public void testConvert() throws IOException {
         System.out.println("Rest Api testConvert"); 
        
        assertTrue(1 == 1);
        
    }
    
}
