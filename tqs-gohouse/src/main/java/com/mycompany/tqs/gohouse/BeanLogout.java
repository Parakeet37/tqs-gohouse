/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import other.CurrentUser;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "logBean")
@RequestScoped
public class BeanLogout {

    @PostConstruct
    public void init() {
        CurrentUser.setId(-1);
        CurrentUser.setUniv(null);
        CurrentUser.setEmail("");
        CurrentUser.setIsUniversity(false);

    }

    public void redirect() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");

    }
}
