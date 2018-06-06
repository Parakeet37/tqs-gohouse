/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbclasses.Property;
import dbclasses.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "beanAddRoom", eager = true)
@ViewScoped
public class BeanAddRoom {

    //Mapa para os endereços com quartos
    private Map<String, Long> enderecoProperty = new HashMap<>();
    //Lista de endereços
    private List<String> enderecos = new ArrayList<>();
    //Set of rooms
    private Set<Room> rooms = new HashSet<>();
    //Email do utilizador
    private String email = "";
    //Descrição do quarto
    private String descricao = "";
    //Renda do quarto
    private int rent = 0;
    //Propriedade selecionada
    private String selectedProperty;
    //Message
    private String message = "";
    //Database handler
    private final DBHandler dbHandler = new DBHandler();
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    //Used to render some controls
    private boolean roomAdded = true;

    /**
     * Empty constructor. Only useful to empty selected prop.
     */
    public BeanAddRoom() {
        selectedProperty = "";
        populateDropDown();
    }

    /**
     * Populate the DropDown menu.
     */
    private void populateDropDown() {

        try {
            Set<Property> tmpProperty = dbHandler.getSingleUser(CurrentUser.email).getOwnedProperties();
            for (Property p : tmpProperty) {
                enderecoProperty.put(p.getAddress(), p.getId());
                enderecos.add(p.getAddress());
            }
        } catch (Exception e) {
            message = "Erro no Utilizador. Não existem propriedades.";
            roomAdded = false;
            showDialog();
        }
    }

    /**
     * Adds the room to the database.
     */
    public void submitRoom() {
        assert rent > 0 && descricao != null;

        try {
            long propID = enderecoProperty.get(selectedProperty);
            boolean created = dbHandler.addRoom(descricao, rent, propID);
            
            if (created) {
                message = "Novo quarto criado!";
                roomAdded = true;
                showDialog();
                clearVars();
            } else {
                message = "Não foi possivel adicionar o quarto!";
                roomAdded = false;
                showDialog();
            }

        } catch (Exception e) {
            message = "Não foi possivel obter a propriedade selecionada";
            roomAdded = false;
            showDialog();
        }

    }

   /**
     * Show a message dialog by executing the javascript.
     */
    private void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("$('.modalPseudoClass').modal();");
    }

    /**
     * Cleans all variables.
     */
    private void clearVars() {
        this.descricao = "";
        this.email = "";
        this.rent = 0;
        this.selectedProperty = "";
    }

    //Getters and Setters
    public Map<String, Long> getEnderecoProperty() {
        return enderecoProperty;
    }

    public void setEnderecoProperty(Map<String, Long> enderecoProperty) {
        this.enderecoProperty = enderecoProperty;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public void setRooms(Set<Room> rooms) {
        this.rooms = rooms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public String getSelectedProperty() {
        return selectedProperty;
    }

    public void setSelectedProperty(String selectedProperty) {
        this.selectedProperty = selectedProperty;
    }

    public List<String> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<String> enderecos) {
        this.enderecos = enderecos;
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

    public boolean isRoomAdded() {
        return roomAdded;
    }

    public void setRoomAdded(boolean roomAdded) {
        this.roomAdded = roomAdded;
    }

    
}
