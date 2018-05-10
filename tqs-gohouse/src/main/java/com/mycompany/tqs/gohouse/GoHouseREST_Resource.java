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

   // Ideally this state should be stored in a database
   @EJB
   DBHandler dbH;

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
   
   @POST
   @Path("register-email:{email},name:{name}")
   public String registerUser(
           @PathParam("email") String email,@PathParam("name") String name) {
       if(dbH.registerUser(email, name, LocalDate.MIN, true, true, new University("", "")))
           return "ok!";
       else return "not ok!";
   }
}