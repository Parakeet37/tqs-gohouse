package com.mycompany.tqs.gohouse;

import dbclasses.Property;
import dbclasses.Room;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import other.Utils;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "homeBean", eager = true)
@ViewScoped
public class HomeBean implements Serializable {

    //Room List
    private List<Room> rooms = new ArrayList<>();
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    //Used to render if the user has a university.
    private boolean hasUniversity = Utils.hasUniversity();
    //Used to render button with text
    private String universityName = Utils.universityName();
    //Database handler
    private final DBHandler dBHandler = new DBHandler();

    /**
     * Starts by loading all rooms. The commented code is for test.
     */
    @PostConstruct
    public void init() {
        loadRooms();

    }

    /**
     * Used by University to rent a room.
     *
     * @param roomId Room id
     * @return Confirmation if it worked or not
     */
    public boolean rentFull(long roomId) {
        //Check if it has any University assossiated.
        return dBHandler.rentRoomToUniversity(roomId, Utils.getUserId());
    }

    /**
     * Loads from all available properties the rooms. TODO Check if property is
     * valid.
     */
    private void loadRooms() {
        List<Property> properties = dBHandler.getAvailableProperties();
        for (Property p : properties) {

            Set<Room> rms = p.getRooms();
            for (Room r : rms) {
                if (!r.isOccupied()) {
                    rooms.add(r);
                }
            }

        }

    }

    //Getters and setters
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isHasUniversity() {
        return hasUniversity;
    }

    public void setHasUniversity(boolean hasUniversity) {
        this.hasUniversity = hasUniversity;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

}
