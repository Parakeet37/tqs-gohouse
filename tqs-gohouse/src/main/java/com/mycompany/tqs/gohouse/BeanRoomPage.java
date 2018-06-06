package com.mycompany.tqs.gohouse;

import dbclasses.Room;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

@ManagedBean(name = "beanroomPage", eager = true)
@ViewScoped
public class BeanRoomPage {

    //Id da propriedade
    private long propertyID = -1;

    //Id do quarto
    private long roomID = -1;

    //Quarto a ser apresentado
    private Room room;

    //Descrição do quarto.
    private String rent = "";
    private String user = "";
    private String description = "";

    //Mensagem a ser apresentada no modal.
    private String message = "";

    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    private boolean canRedirect = false;
     //Database handler
    @Inject
    private DBHandler dBHandler;

    /**
     * Post constructor. Gets the parameters from the URL with the Id of the
     * room and property.
     */
    @PostConstruct
    public void init() {
        //Get the parameters from the url and initialize vars
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

            propertyID = Long.parseLong(params.get("paramProp").split("z")[0]);
            roomID = Long.parseLong(params.get("paramProp").split("z")[1]);

            room = new Room();

        } catch (NumberFormatException e) {
            Logger.getLogger("Não foram lidos nenhuns parametros ou estes estão mal passados... not the food");
        }

        populateView();
    }

    /**
     * Puts all the information of the room in the variable Room that is used in
     * the XHTML.
     */
    private void populateView() {
        assert propertyID != -1 && roomID != -1;

        //Get the room we want
        try {
            Set<Room> listRooms = dBHandler.getPropertyByID(propertyID).getRooms();
            for (Room r : listRooms) {

                if (r.getId() == roomID) {
                    room = r;
                    //No need to go further.
                    break;
                }
            }

            rent = room.getRent() + "";
            user = room.getProperty().getOwner().getName();
            description = room.getDescription();
        } catch (Exception e) {
            message = "Ups...Nenhum quarto foi encontrado. Por favor volte à página de inicio.";
            showDialog();
        }

    }

    /**
     * Rents the room given some circumstances.
     *
     * @throws IOException HomePage not found.
     */
    public void rentRoom() throws IOException {

        //Verificar se o user está registado.
        if (CurrentUser.ID == -1 || room == null) {
            message = "Ups...Parece que não está registado.";
            showDialog();

        } else {
            boolean regist = dBHandler.rentRoomToUser(roomID, CurrentUser.ID);
            if (regist) {
                message = "Obrigado por utilizar a nossa plataforma como meio de arrendamento de quartos.\n"
                        + "Este é o seu novo quarto : " + room.getProperty().getAddress() + "\n"
                        + "Piso: " + room.getProperty().getFloor() + "\n"
                        + "Bloco: " + room.getProperty().getBlock() + "\n";
                //In case it is owned by a university.
                if (room.getUniversity() != null) {
                    message += "\n\n\n";
                    message += room.getUniversity().getName();
                }
                showDialog();
                canRedirect = true;
                //Redirect to the HomePage

            } else {
                message = "Ups...Por alguma razão não foi possivel arrendar o quarto.";
                showDialog();
            }
        }
    }

    /**
     * To redirect to home page
     *
     * @throws IOException
     */
    public void redirect() throws IOException {
        if (canRedirect) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
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
