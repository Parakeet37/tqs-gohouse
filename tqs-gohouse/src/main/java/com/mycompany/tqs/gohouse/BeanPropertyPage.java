/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbclasses.Property;
import dbclasses.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "beanpropertyPage", eager = true)
@ViewScoped
public class BeanPropertyPage {

    //Propriedade a ser vista
    private Property propriedade;
    // Id da propriedade
    private long id;
    //Database handler
    private final DBHandler dBHandler = new DBHandler();

    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    //List of rooms
    private List<Room> rooms = new ArrayList<>();
    //Used to render if the user has a university.
    private boolean hasUniversity = Utils.hasUniversity();
    //Used to render button with text
    private String universityName = Utils.universityName();
    //Message 
    private String message = "";
    private boolean roomRented = true;

    /**
     * When the bean is created; Get the parameter id which is the id of the
     * property we are viewing.
     */
    @PostConstruct
    public void init() {
        //Get the parameters from the url
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            id = Long.parseLong(params.get("propertyId"));
            propriedade = new Property();
        } catch (NumberFormatException e) {
            Logger.getLogger("Could not parse values");
        }
        //Populate view
        populateView();

    }

    /**
     * Gets the property information.
     */
    private void populateView() {
        assert id >= 0;
        this.propriedade = dBHandler.getPropertyByID(id);
        Set<Room> tmp = propriedade.getRooms();
        for(Room r: tmp){
            rooms.add(r);
        }
    }

    /**
     * Used by University to rent a room.
     *
     * @param roomId Room id
     */
    public void rentFull(long roomId) {
        //Check if it has any University assossiated.
        if (CurrentUser.getUniv() != null) {
            boolean sucess = dBHandler.rentRoomToUniversity(roomId, CurrentUser.getId());
            if (sucess) {
                message = "Quarto foi arrendade á universidade " + universityName;
                roomRented = true;
                showDialog();
            }else{
                message = "Não foi possivel arrendar o quarto!";
                roomRented = false;
                showDialog();
            }
        }

    }

    /**
     * Show a message dialog by executing the javascript.
     */
    private void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("$('.modalPseudoClass').modal();");
    }

    //Getters and setters
    public Property getPropriedade() {
        return propriedade;
    }

    public void setPropriedade(Property propriedade) {
        this.propriedade = propriedade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRoomRented() {
        return roomRented;
    }

    public void setRoomRented(boolean roomRented) {
        this.roomRented = roomRented;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    

}
