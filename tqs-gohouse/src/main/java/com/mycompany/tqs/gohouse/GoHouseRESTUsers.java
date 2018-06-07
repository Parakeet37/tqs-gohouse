package com.mycompany.tqs.gohouse;

import dbclasses.PlatformUser;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
@RequestScoped
public class GoHouseRESTUsers {

   @EJB
   DBHandler dbH;

   /**
    * Lists all users
    * @return JSON detailing all users
    */
   @GET
   @Produces({"application/json"})
   public List<PlatformUser> listAllUsers() {
       return dbH.getNMostPopularUsers(0);
   }
   
   
   /**
    * Lists 1 user, searched by his email
    * @param email A user's email
    * @return JSON detailing a user
    */
   @GET
   @Path("{email}")
   @Produces({"application/json"})
   public PlatformUser getUserByEmail(@PathParam("email") String email){
    return dbH.getSingleUser(email);
   }
   
   /**
    * Lists the most popular users, limited by a max number
    * @param number The number of users to present
    * @return JSON detailing some users
    */
   @GET
   @Path("popular:{number}")
   @Produces({"application/json"})
   public List<PlatformUser> listNPopularUsers(@PathParam("number") int number) {
       return dbH.getNMostPopularUsers(number);
   }
   
   
   /**
    * Registers a user, can be a Delegate
    * @param email User's email
    * @param name User's name
    * @param isDelegate Sets if is delegate or not
    * @param password The user password 
     * @return Failure or success JSON message
    */
   @POST
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public String registerUser(@FormParam("password") String password, 
           @FormParam("email") String email,
           @FormParam("name") String name,
           @FormParam("isDelegate") boolean isDelegate) {
       
       if(dbH.getSingleUser(email) == null){
           dbH.registerUser(password, email, name, LocalDate.now(), isDelegate);
           return "{\"success\":true, \"stateMsg\":\"No problem here.\"}";
       }
       else{
            return "{\"success\":false, \"stateMsg\":\"Your're already signed up!\"}";
        }
   }

}
