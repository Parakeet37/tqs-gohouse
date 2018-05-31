package com.mycompany.tqs.gohouse;

import dbclasses.Room;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "homeBean", eager = true)
@Singleton
public class HomeBean implements Serializable {

    //Variables
    protected List<Room> rooms = new ArrayList<>();

    //Database handler
    private final DBHandler dBHandler = new DBHandler();

    @PostConstruct
    public void init() {
        loadRooms();
    }

    /**
     * Empty constructor
     */
    public HomeBean() {
        //dBHandler.registerUser("joao@outlook.com", "Joao Serpa", LocalDate.of(1997,7,7), true);
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
