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
import javax.ws.rs.*;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

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
    * @param userRes
    * @return Failure or success JSON message
    */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
   public POSTUser registerUser(POSTUser userRes) {
       
       System.out.println("----ADDING " + userRes.getEmail() + "---\n\n");
        if (getUserByEmail(userRes.getEmail()) == null){
            System.out.println("NEW USER!");
            dbH.registerUser(userRes.getPassword(), userRes.getEmail(), userRes.getName()
            , LocalDate.now(), userRes.isIsDelegate());
            return userRes;
        }
        else {
            System.out.println("User is already registered!");
            return null;
        }
   }

}
