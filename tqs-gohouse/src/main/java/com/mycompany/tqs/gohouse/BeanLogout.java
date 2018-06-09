package com.mycompany.tqs.gohouse;

import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "logBean")
@RequestScoped
public class BeanLogout {

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.clear();

    }

    public void redirect() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");

    }
}
