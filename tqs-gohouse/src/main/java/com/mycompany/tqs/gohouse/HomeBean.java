package com.mycompany.tqs.gohouse;

import dbClasses.Room;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "homeBean", eager = true)
@SessionScoped
public class HomeBean implements Serializable {
 
    //Variables
    protected List<Room> rooms;

    //Database handler
    private final DBHandler dBHandler = new DBHandler();

    @PostConstruct
    public void init() {
        loadRooms();
    }

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
