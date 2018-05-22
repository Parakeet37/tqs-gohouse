
package com.mycompany.tqs.gohouse;


import dbClasses.Property;
import dbClasses.Room;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "loggingBean", eager = true)
@SessionScoped
public class LoggingBean implements Serializable {
    
    //Database handler
    private final DBHandler dbHandler;
    private String userName;
    private String userMail;
    
    //Constructor
    public LoggingBean() {
        dbHandler = new DBHandler();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    

    public void listenerAction(){
        System.out.println("Getting Login Values. (Google VERY LIKELY will remember if you logged in a previous time)");
        
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        userName  = params.get("name");
        userMail  = params.get("email");
        
        System.out.println(userName +"\t"+ userMail);
    }
}



