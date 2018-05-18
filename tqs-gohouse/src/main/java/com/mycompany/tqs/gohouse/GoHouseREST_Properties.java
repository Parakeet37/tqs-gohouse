package com.mycompany.tqs.gohouse;

import dbClasses.Property;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("properties")
@RequestScoped
public class GoHouseREST_Properties {

   @EJB
   DBHandler dbH;

   /**
    * Lists all properties
    * @return JSON list of properties
    */
   @GET
   @Produces({"application/json"})
   public List<Property> listAllUsers() {
       return dbH.getAvailableProperties();
   }
   
   
   
}
