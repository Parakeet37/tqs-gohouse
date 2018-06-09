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
   public UserRes registerUser(UserRes userRes) {
       
       System.out.println("----ADDING " + userRes.getEmail() + "---\n\n");
       
       if(dbH.getSingleUser(userRes.getEmail()) == null)
           dbH.registerUser(userRes.getPassword(), userRes.getEmail(), userRes.getName(), LocalDate.now(), userRes.isIsDelegate());
       return userRes;
   }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    private class UserRes {

         private String email;
         private String name;
         private String password;
         private boolean isDelegate;

        public UserRes(String email, String name, String password, String isDelegate) {
            this.email = email;
            this.name = name;
            this.password = password;
            this.isDelegate = Boolean.parseBoolean(isDelegate);
        }
        
        UserRes(){
            this.email = "";
            this.name = "";
            this.password = "";
            this.isDelegate = false;
        }
         
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isIsDelegate() {
            return isDelegate;
        }

        public void setIsDelegate(boolean isDelegate) {
            this.isDelegate = isDelegate;
        }
         

         
    }
}
