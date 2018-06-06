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
import other.CurrentUser;
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
        //Test -------
        /*dBHandler.registerUser("joaoserpa@ua.pt", "dsad", LocalDate.of(1997, 1, 1), true);

        List<PlatformUser> d = dBHandler.getNMostPopularUsers(90);
        for (PlatformUser u : d) {
            if (u.getEmail().equals("joaoserpa@ua.pt")) {
                System.out.println("ISss " + u.getId());
                CurrentUser.ID = u.getId();
                CurrentUser.email = "joaoserpa@ua.pt";
                System.out.println("Email " + "joaoserpa@ua.pt" + "    ID " + CurrentUser.ID + " ddd " + CurrentUser.email);
            }
        }
        
        dBHandler.addNewProperty(CurrentUser.ID, 0.0f, 0.0f, "Teste address", PropertyType.APARTMENT, 'c', 1);
        dBHandler.addRoom("Description", 10090, 2);
        dBHandler.addRoom("Description", 1000, 2);
        CurrentUser.univ = new University("dddd", "add");*/
        //End of Test -------

        loadRooms();

    }

    /**
     * Used by University to rent a room.
     *
     * @param roomId Room id
     */
    public void rentFull(long roomId) {
        //Check if it has any University assossiated.
        if (CurrentUser.univ != null) {
            boolean sucess = dBHandler.rentRoomToUniversity(roomId, CurrentUser.ID);
            if (sucess) {
                System.out.println("RENTEEEEEEEEEEEEEEEEEEEEEED");
            }
        }

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
