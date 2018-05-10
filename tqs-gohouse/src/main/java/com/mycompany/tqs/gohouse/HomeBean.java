/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

/**
 *
 * @author demo
 */
import dbClasses.Property;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "homeBean", eager = true)
@SessionScoped
@Singleton
public class HomeBean implements Serializable {

    //Variables
    protected List<Property> propriedades;

    //Constructor
    public HomeBean() {
        this.loadProperties();
    }

    //Has to connect to the database and get all the information from Propriedades
    private void loadProperties() {
        System.err.println("Not done yet.");
    }

    public List<Property> getPropriedades() {
        return propriedades;
    }

    public void setPropriedades(List<Property> propriedades) {
        this.propriedades = propriedades;
    }

}
