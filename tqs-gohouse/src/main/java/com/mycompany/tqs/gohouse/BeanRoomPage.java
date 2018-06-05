package com.mycompany.tqs.gohouse;

import dbclasses.Room;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

/**
 *
 * @author Joao
 */
@ManagedBean(name = "beanroomPage", eager = true)
@ViewScoped
public class BeanRoomPage {

    private long propertyID = -1;
    private long roomID = -1;
    private Room room;
    private String rent = "";
    private String user = "";
    private String description = "";
    private String message = "";
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    private final DBHandler dBHandler = new DBHandler();

    @PostConstruct
    public void init() {
        //Get the parameters from the url and initialize vars
        try {

            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            propertyID = Long.parseLong(params.get("paramProp").split("z")[0]);
            roomID = Long.parseLong(params.get("paramProp").split("z")[1]);
            System.out.println("I READ                 " + roomID);

            room = new Room();

        } catch (NumberFormatException e) {
            System.err.println("Could not retrieve the parametres or parse them");
        }

        populateView();
    }

    /**
     * Puts all the information.
     */
    private void populateView() {
        assert propertyID != -1 && roomID != -1;
        //Get the room we want

        try {
            Set<Room> rooms = dBHandler.getPropertyByID(propertyID).getRooms();
            System.out.println("TOOOO  " + rooms.size());
            for (Room r : rooms) {

                if (r.getId() == roomID) {
                    System.out.println("Yooo " + r.getId());
                    room = r;
                    break;
                }
            }

            rent = room.getRent() + "";
            user = room.getProperty().getOwner().getName();
            description = room.getDescription();
        } catch (Exception e) {
            message = "No Room available.";
            showDialog();
        }

    }

    /**
     * Rents a room.
     */
    public void rentRoom() throws IOException {

        if (CurrentUser.ID == -1 || room == null) {
            message = "Não está registado.";
            showDialog();

        } else {
            boolean regist = dBHandler.rentRoomToUser(roomID, CurrentUser.ID);
            if (regist) {
                message = "Quarto arrendado";
                showDialog();

                FacesContext.getCurrentInstance().getExternalContext().redirect("faces/home.xhtml");

            } else {
                message = "Não foi possivel arrendar o quarto.";
                showDialog();
            }
        }
    }

    /**
     * Show a message dialog.
     */
    private void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlg1').show();");
    }

    public long getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(long propertyID) {
        this.propertyID = propertyID;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

}
