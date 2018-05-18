package com.mycompany.tqs.gohouse;

import dbClasses.PlatformUser;
import dbClasses.Property;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * This class is to be used in userDetailsPage
 * @author joaos
 */
@ManagedBean(name = "userDetailsBean", eager = true)
@SessionScoped
public class Bean_UserDetails {
    
    //This is the user we are viewing
    private PlatformUser userPlatform = new PlatformUser();
    //email from the params
    private String email;
    //Database handler
    private DBHandler dbHandler = new DBHandler();
    //List of properties from the user
    private List<Property> listaDePropriedades;
    
    @PostConstruct
    public void init(){
        //Get the parameters from the url
        try{
            Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            email = params.get("id");
            listaDePropriedades = new ArrayList<>();
            
        }catch(Exception e){
            System.err.println("Could not retrieve the parametres");
        }
        populateView();
    }
    
    
    //Display's all of the user information
    private void populateView(){
       
        this.userPlatform = dbHandler.getSingleUser(email);
        if (userPlatform == null) {
            System.out.println("YHEAAAAAAAAAAA");
        }
        //System.out.println(this.userPlatform.getEmail());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PlatformUser getUserPlatform() {
        return userPlatform;
    }

    public void setUserPlatform(PlatformUser userPlatform) {
        this.userPlatform = userPlatform;
    }

    public List<Property> getListaDePropriedades() {
        return listaDePropriedades;
    }

    public void setListaDePropriedades(List<Property> listaDePropriedades) {
        this.listaDePropriedades = listaDePropriedades;
    }
    
    
    
    
    
}
