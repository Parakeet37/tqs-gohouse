package com.mycompany.tqs.gohouse;

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

@Path("currencies")
@RequestScoped
public class CurrencyResource {

   // Ideally this state should be stored in a database
   @EJB
   HomeBean hB;

   @GET
   @Produces({ "application/xml", "application/json" })
   public String listAllCurrencies() {
       return "<a>asdf2</a>";
   }
   
   @EJB
   Requester req;
   
   @GET
   @Produces({ "application/json", "application/xml" })
   @Path("{value}:{curr1}to{curr2}")
   public String calculate(@PathParam("value") String value, 
           @PathParam("curr1") String curr1, @PathParam("curr2") String curr2)
   {
      return "<a>wee</a>";
   }
}
