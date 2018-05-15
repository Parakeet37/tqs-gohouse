
package com.mycompany.tqs.gohouse;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "homeBean", eager = true)
@SessionScoped
@Singleton
public class HomeBean implements Serializable {

    //Variables
    protected List<TempPropriadade> propriedades;

    //Constructor
    public HomeBean() {
        this.loadProperties();
    }

    //Has to connect to the database and get all the information from Propriedades
    private void loadProperties() {
        this.propriedades = new ArrayList<>();
        TempPropriadade t;
        t = new TempPropriadade("Joao", "11", "Desc", 19.8f, "IDK", 0, "https://t-ec.bstatic.com/images/hotel/max1024x768/728/72898182.jpg");
        propriedades.add(t);
        TempPropriadade p;
        p = new TempPropriadade("Joao121", "Titulo 2", "Descrips", 111111.8f, "Too Expensive", 1, "https://t-ec.bstatic.com/images/hotel/max1024x768/728/72898182.jpg");
        propriedades.add(p);

    }

    public List<TempPropriadade> getPropriedades() {
        return propriedades;
    }

    public void setPropriedades(List<TempPropriadade> propriedades) {
        this.propriedades = propriedades;
    }

    
    
}



