package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.University;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
public class GoHouseREST_Resource {

   @EJB
   DBHandler dbH;

   /**
    * Lista todos os utilizadores
    * @return = String JSON listando os utilizadores
    */
   @GET
   @Path("list")
   @Produces({"application/json"})
   public String listAllUsers() {
       List<PlatformUser> pU = dbH.getAllUsers();
       String out = "{\n"
               + "\"users\": [\n";
       for (PlatformUser p: pU){
           out = out.concat(String.format("{\n"
                + "\"email\": \"%s\",\n"
                + "\"name\": \"%s\",\n"
                + "\"age\": \"%d\"\n},", p.getEmail(),p.getName(),p.getAge()));
       }
       out = out.substring(0, out.length()-1) + "]\n}";
       return out;
   }
   
   
   /**
    * Lista 1 utilizador, pelo seu email
    * @param email Email para procurar 1 utilizador
    * @return 1 utilizador, em formato JSON
    */
   @GET
   @Path("getOne-email:{email}")
   @Produces({"application/json"})
   public String getOneUser(@PathParam("email") String email){
           PlatformUser p = dbH.getSingleUser(email);
       String out = String.format("{\n"
               + "\"user\": [\n{\n"
                + "\"email\": \"%s\",\n"
                + "\"name\": \"%s\",\n"
                + "\"age\": \"%d\"\n}]\n}", p.getEmail(),p.getName(),p.getAge());
       
   return out;
   }
   
   /**
    * Lista os utilizadores mais populares, limitado pelo 'number'.
    * @param number O numero de utilizadores populares para revelar
    * @return Os utilizadores mais populares
    */
   @GET
   @Path("listPopular-n:{number}")
   @Produces({"application/json"})
   public String listPopularUsers(@PathParam("number") String number) {
       List<PlatformUser> pU = dbH.getNMostPopularUsers(Integer.parseInt(number));
       String out = "{\n"
               + "\"users\": [\n";
       for (PlatformUser p: pU){
           out = out.concat(String.format("{\n"
                + "\"email\": \"%s\",\n"
                + "\"name\": \"%s\",\n"
                + "\"age\": \"%d\"\n},", p.getEmail(),p.getName(),p.getAge()));
       }
       out = out.substring(0, out.length()-1) + "]\n}";
       return out;
   }
   
   /**
    * Regista um utilizador, definindo o seu Email e nome.
    * @param email O Email do utilizador novo.
    * @param name O nome do utilizador novo.
    * @return Mensagem confirmando se o utilizador foi criado.
    */
   @POST
   @Path("register-email:{email},name:{name}")
   @Produces({"application/json"})
   public String registerUser(@PathParam("email") String email,@PathParam("name") String name) {
        return (dbH.registerUser(email, name, LocalDate.MIN, true, true, new University("", "")) == true)
                ? "{\"success\":true}" : "{\"success\":false}";
   }

}