/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import dbClasses.Property;
import java.util.ArrayList;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "beanpropertyPage", eager = true)
@SessionScoped
public class BeanPropertyPage {
    
    
    //Propriedade a ser vista
    private Property propriedade;
    // Id da propriedade
    private long id;
    //Database handler
    private DBHandler dBHandler = new DBHandler();
    
    /**
     * When the bean is created;
     * Get the parameter id which is the id of the property we are viewing.
     */
    @PostConstruct
    public void init(){
        //Get the parameters from the url
        try{
            Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            id = Long.parseLong(params.get("propertyId"));
            propriedade = new Property();
            
        }catch(NumberFormatException e){
            System.err.println("Could not retrieve the parametres or parse them");
        }
        //Populate view
        populateView();
        
    }
    
    /*
    * Gets the property information.
    */
    private void populateView(){
        assert id >= 0;
        //this.propriedade = dBHandler.getPropertyByID(id);
    }

    
    
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

    public DBHandler getdBHandler() {
        return dBHandler;
    }

    public void setdBHandler(DBHandler dBHandler) {
        this.dBHandler = dBHandler;
    }
    
    
    
    
}
