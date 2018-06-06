package com.mycompany.tqs.gohouse;

import com.sun.istack.internal.logging.Logger;
import dbclasses.PlatformUser;
import dbclasses.Property;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import other.Utils;

@ManagedBean(name = "userDetailsBean", eager = true)
@Singleton
public class Bean_UserDetails {

    //This is the user we are viewing
    private PlatformUser userPlatform = new PlatformUser();
    //email from the params
    private String email;
    //Database handler
    private final DBHandler dbHandler = new DBHandler();
    //List of properties from the user
    private List<Property> listaDePropriedades;
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();

    /**
     * When the bean is created, get the parameter <b>id</b> which is the email
     * of the user we are viewing.
     */
    @PostConstruct
    public void init() {

        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            email = params.get("id");
            listaDePropriedades = new ArrayList<>();
        } catch (Exception e) {
            Logger.getLogger(Bean_UserDetails.class).log(Level.SEVERE, "Could not get the parameter ID");
        }

        populateView();
    }

    /**
     * Displays all the user information. Goes to the database and checks the
     * user by email and then all its properties.
     *
     */
    private void populateView() {

        this.userPlatform = dbHandler.getSingleUser(email);
        if (userPlatform != null) {
            List<Property> tempProp = dbHandler.getAvailableProperties();
            for (int i = 0; i < tempProp.size(); i++) {
                Property pr = tempProp.get(i);
                if (pr.getOwner().equals(userPlatform)) {
                    listaDePropriedades.add(pr);
                }
            }
        }
    }

    //Getters and setters
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

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

}
