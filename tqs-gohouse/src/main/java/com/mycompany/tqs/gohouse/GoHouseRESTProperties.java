package com.mycompany.tqs.gohouse;

import dbclasses.Property;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("properties")
@RequestScoped
public class GoHouseRESTProperties {

   @EJB
   DBHandler dbH;

   /**
    * Lists all properties
    * @return JSON list of properties
    */
   @GET
   @Produces({"application/json"})
   public List<Property> listAllProperties() {
       return dbH.getAvailableProperties();
   }
   
    /**
    * Lists all unverified properties
    * @return JSON list of properties
    */
   @GET
   @Path("unverified")
   @Produces({"application/json"})
   public List<Property> listUnverifiedProperties() {
       return dbH.getUnverifiedProperties();
   }
   
      /**
    * Sets a rating.
    * @param id The user's ID
    * @param rate The rate said user will attribute
     * @return 
    */
   @POST
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   @Path("rate")
   public String rateApartment(@FormParam("id") int id,
           @FormParam("rate") int rate) {
       try{
           dbH.giveRatingToProperty((long)id,rate);
           return "{\"success\":true, \"stateMsg\":\"No problem here.\"}";
       }
        catch(Exception e){
            return "{\"success\":true, \"stateMsg\":\""+ e.getMessage() +"\"}";
        }
   }
   
}
