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
import javax.ws.rs.PUT;
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
    * Lista todos os utilizadores
    * @return = String JSON listando os utilizadores
    */
   @GET
   @Produces({"application/json"})
   public List<PlatformUser> listAllUsers() {
       return dbH.getNMostPopularUsers(90);
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
   @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   public void registerUser(@FormParam("email") String email,
           @FormParam("name") String name,
           @FormParam("isDelegate") boolean isDelegate) {
        System.out.println("Creating " + name + "...");
        dbH.registerUser(email, name, LocalDate.now(), isDelegate);
   }

   @PUT
   public void putUser(@FormParam("email") String email,
           @FormParam("name") String name,
           @FormParam("isDelegate") boolean isDelegate) {
      registerUser(email, name, isDelegate);
   }

}
