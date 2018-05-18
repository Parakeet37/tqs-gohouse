package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("users")
@RequestScoped
public class GoHouseRESTUsers {

   @EJB
   DBHandler dbH;

   /**
    * Lista todos os utilizadores
    * @return = String JSON listando os utilizadores
    */
   @GET
   @Produces({"application/json"})
   public List<PlatformUser> listAllUsers() {
       dbH.registerUser("aa", "MYNAME", LocalDate.now(), true);
       return dbH.getNMostPopularUsers(3);
   }
   
   
   /**
    * Lista 1 utilizador, pelo seu email
    * @param email Email para procurar 1 utilizador
    * @return 1 utilizador, em formato JSON
    */
   @GET
   @Path("{email}")
   @Produces({"application/json"})
   public PlatformUser getUserByEmail(@PathParam("email") String email){
    return dbH.getSingleUser(email);
   }
   
   /**
    * Lista os utilizadores mais populares, limitado pelo 'number'.
    * @param number O numero de utilizadores populares para revelar
    * @return Os utilizadores mais populares
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
    */
   @POST
   public void registerUser(@PathParam("email") String email,
           @PathParam("name") String name, 
           @PathParam("isDelegate") boolean isDelegate) {
       
        dbH.registerUser(email, name, LocalDate.now(), isDelegate);
   }

   /**
    * Sets a rating.
    * @param id The user's ID
    * @param rate The rate said user will attribute
    */
   @POST
   public void rateApartment(@PathParam("id") long id,
           @PathParam("rate") int rate) {
       
        dbH.giveRatingToProperty(id,rate);
   }
}
