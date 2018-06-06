package com.mycompany.tqs.gohouse;

import dbclasses.PropertyType;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "beanAddPropriedade", eager = true)
@ViewScoped
public class BeanAddPropriedade {

    private String id = CurrentUser.ID + "";
    private String latitude;
    private String longitude;
    private String endereco;
    private String bloco;
    private String piso;
    private String tipoPropriedade;
    private String message;
    //Boolean to render the button in modal
    private boolean propAdded = true;

    //Database handler
    private final DBHandler dBHandler = new DBHandler();
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();

    /**
     * Empty constructor that just clears the message field.
     */
    public BeanAddPropriedade() {
        this.message = "";

    }

    /**
     * Submits a property to the database.
     */
    public void submitProperty() {
        //Some asserts
        assert id != null && latitude != null && longitude != null && endereco != null && bloco != null && piso != null && tipoPropriedade != null;
        assert !"".equals(id) && !"".equals(latitude) && !"".equals(longitude) && !"".equals(endereco) && !"".equals(bloco) && !"".equals(piso) && !"".equals(tipoPropriedade);

        try {
            char bloc = bloco.toCharArray()[0];

            boolean created;
            if ("Casa".equals(tipoPropriedade)) {
                created = dBHandler.addNewProperty(CurrentUser.ID, Float.parseFloat(longitude), Float.parseFloat(latitude), endereco, PropertyType.HOUSE, bloc, Integer.parseInt(piso));
            } else {
                created = dBHandler.addNewProperty(CurrentUser.ID, Float.parseFloat(longitude), Float.parseFloat(latitude), endereco, PropertyType.APARTMENT, bloc, Integer.parseInt(piso));
            }

            if (created) {
                message = "A propriedade for criada. Endereço: " + endereco;
                propAdded = true;

            } else {
                message = "Propriedade não criada, utilizador não existente!";
                propAdded = false;
            }

            showDialog();
            //Clear all values
            clearVars();

        } catch (NumberFormatException e) {
            message = "Erro na informação. Alguns valores estão incorretos.";
            propAdded = false;
            showDialog();
        }

    }

    /**
     * Method that cleans all variables. USAGE: After the submit
     */
    private void clearVars() {
        id = "";
        latitude = "";
        longitude = "";
        endereco = "";
        bloco = "";
        piso = "";
        tipoPropriedade = "";
        
    }

    /**
     * Show a message dialog by executing the javascript.
     */
    private void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("$('.modalPseudoClass').modal();");
    }


    //Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getTipoPropriedade() {
        return tipoPropriedade;
    }

    public void setTipoPropriedade(String tipoPropriedade) {
        this.tipoPropriedade = tipoPropriedade;
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

    public boolean isPropAdded() {
        return propAdded;
    }

    public void setPropAdded(boolean propAdded) {
        this.propAdded = propAdded;
    }

}
