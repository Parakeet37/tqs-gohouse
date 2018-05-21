
package com.mycompany.tqs.gohouse;


import dbClasses.Property;
import dbClasses.Room;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "homeBean", eager = true)
@SessionScoped
@Singleton
public class HomeBean implements Serializable {
    
    //Database handler
    private final DBHandler dbHandler;
    private String nameSignIn;
    private String mailSignIn;
    
    //Constructor
    public HomeBean() {
        dbHandler = new DBHandler();
    }

    public String getNameSignIn() {
        return nameSignIn;
    }

    public void setNameSignIn(String nameSignIn) {
        this.nameSignIn = nameSignIn;
    }

    public String getMailSignIn() {
        return mailSignIn;
    }

    public void setMailSignIn(String mailSignIn) {
        this.mailSignIn = mailSignIn;
    }

}



