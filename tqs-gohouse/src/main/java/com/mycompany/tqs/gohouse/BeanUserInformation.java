package com.mycompany.tqs.gohouse;

import dbclasses.PlatformUser;
import dbclasses.Room;
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
@ManagedBean(name = "beanUserInfo")
@ViewScoped
public class BeanUserInformation {

    //This is Us
    private PlatformUser userPlatform = new PlatformUser();
    //Database handler
    private final DBHandler dbHandler = new DBHandler();
    //List of properties from the user
    private List<Room> roomsList = new ArrayList<>();
    //Nomes dos senhorios
    private List<String> senhorios = new ArrayList<>();
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    //Propriedade selecionada
    private String selectedSenhorio;
    // Rating to give
    private int rating;

    /**
     * When the bean is created.
     */
    @PostConstruct
    public void init() {
        userPlatform = dbHandler.getSingleUser(CurrentUser.email);
        populateView();
    }

    /**
     * Populates the views
     */
    private void populateView() {
        Set<Room> tmp = dbHandler.getSingleUser(CurrentUser.email).getRentedRooms();
        for (Room r : tmp) {
            roomsList.add(r);
            if (!senhorios.contains(r.getProperty().getId() + "," + r.getProperty().getAddress())) {
                senhorios.add(r.getProperty().getId() + "," + r.getProperty().getAddress());
            }
        }
    }

    /**
     * Submits the rating
     */
    public void submitRating() {
        assert !"".equals(selectedSenhorio);
        dbHandler.giveRatingToProperty(Integer.parseInt(selectedSenhorio.split(",")[0]), rating);
    }

    //Getter and setters
    public PlatformUser getUserPlatform() {
        return userPlatform;
    }
    
    public void setUserPlatform(PlatformUser userPlatform) {
        this.userPlatform = userPlatform;
    }
    
    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }
    
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    public List<Room> getRoomsList() {
        return roomsList;
    }
    
    public void setRoomsList(List<Room> roomsList) {
        this.roomsList = roomsList;
    }
    
    public String getSelectedSenhorio() {
        return selectedSenhorio;
    }
    
    public void setSelectedSenhorio(String selectedSenhorio) {
        this.selectedSenhorio = selectedSenhorio;
    }
    
    public List<String> getSenhorios() {
        return senhorios;
    }
    
    public void setSenhorios(List<String> senhorios) {
        this.senhorios = senhorios;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    
}
