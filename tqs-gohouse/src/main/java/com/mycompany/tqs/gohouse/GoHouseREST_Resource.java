package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import java.util.ArrayList;
import java.util.List;
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

@Path("listUsers")
@RequestScoped
public class GoHouseREST_Resource {

   // Ideally this state should be stored in a database
   //@EJB
   //HomeBean hB;

   @GET
   @Produces({"application/json"})
   public String listAllUsers() {
       PlatformUser[] pU = {
        new PlatformUser("scrimminybingus@scrunch","Mart Robbert",40,true,false,false,0),
        new PlatformUser("gringledoof@scrunchy","Some fricking boy",9,true,false,false,0),
        new PlatformUser("funnyMan22@scrunched","Billy",20,true,false,false,0),
        new PlatformUser("GigaLith@scrunchal","Tim Dingus",25,true,false,false,0)
       };
       
       String out = "{\n"
               + "\"users\": [\n";
       for (int i = 0; i < pU.length; i++){
           out = out.concat(String.format("{\n"
                + "\"email\": \"%s\",\n"
                + "\"name\": \"%s\",\n"
                + "\"age\": \"%d\"\n}", pU[i].getEmail(),pU[i].getName(),pU[i].getAge()));
           if (i != pU.length - 1) out = out.concat(",");
           else out = out.concat("]\n}");
       }
       return out;
   }
}