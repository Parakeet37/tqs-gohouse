package com.mycompany.tqs.gohouse;

import dbclasses.Property;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
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
     * @param postRate A POSTPropertyRate object, acquired via JSON request
     * @return Failure or success JSON message
    */
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("rate")
   public POSTPropertyRate verifyApartment(POSTPropertyRate postRate) {
       try{
           dbH.verifyProperty(postRate.getDelegate(),postRate.getId(),postRate.getRate());
           return postRate;
       }
        catch(Exception e){
            return null;
        }
   }
   
}
