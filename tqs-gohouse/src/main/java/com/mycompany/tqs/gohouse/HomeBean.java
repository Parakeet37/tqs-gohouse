
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
 
    //Variables
    protected List<Room> rooms;
    
    //Database handler
    private final DBHandler dBHandler = new DBHandler();
    
    //Constructor
    public HomeBean() {
        this.loadRooms();
    }

    //Has to connect to the database and get all the information from Propriedades
    private void loadRooms() {
        rooms = dBHandler.getAvailableRooms();        
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

}



