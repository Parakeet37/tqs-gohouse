package com.mycompany.tqs.gohouse;

import dbClasses.PropertyType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "beanAddPropriedade", eager = true)
@SessionScoped
public class BeanAddPropriedade {

    private String ID;
    private String Latitude;
    private String Longitude;
    private String Endereço;
    private String Bloco;
    private String Piso;
    private String TipoPropriedade;

    private final DBHandler dBHandler = new DBHandler();

    /**
     * Empty constructor
     */
    public BeanAddPropriedade() {
    }

    public void submitProperty() {
        assert ID != null && Latitude != null && Longitude != null && Endereço != null && Bloco != null && Piso != null && TipoPropriedade != null;
        assert ID != "" && Latitude != "" && Longitude != "" && Endereço != "" && Bloco != "" && Piso != "" && TipoPropriedade != "";
        try {
            char bloc = Bloco.toCharArray()[0];

            if ("Casa".equals(TipoPropriedade)) {

                dBHandler.addNewProperty(Integer.parseInt(ID), Float.parseFloat(Longitude), Float.parseFloat(Latitude), Endereço, PropertyType.HOUSE, bloc, Integer.parseInt(Piso), null);
            } else {

                dBHandler.addNewProperty(Integer.parseInt(ID), Float.parseFloat(Longitude), Float.parseFloat(Latitude), Endereço, PropertyType.APARTMENT, bloc, Integer.parseInt(Piso), null);
            }
            
            System.out.println("Added property with ID " + ID);
            //Clear all values
            clearVars();

        } catch (NumberFormatException e) {
        }

    }

    /**
     * Method that cleans all variables.
     * USAGE: After the submit
     */
    private void clearVars() {
        ID = "";
        Latitude = "";
        Longitude = "";
        Endereço = "";
        Bloco = "";
        Piso = "";
        TipoPropriedade = "";
    }

    //Getters and setters
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getEndereço() {
        return Endereço;
    }

    public void setEndereço(String Endereço) {
        this.Endereço = Endereço;
    }

    public String getBloco() {
        return Bloco;
    }

    public void setBloco(String Bloco) {
        this.Bloco = Bloco;
    }

    public String getTipoPropriedade() {
        return TipoPropriedade;
    }

    public void setTipoPropriedade(String TipoPropriedade) {
        this.TipoPropriedade = TipoPropriedade;
    }

    public String getPiso() {
        return Piso;
    }

    public void setPiso(String Piso) {
        this.Piso = Piso;
    }

}
