package com.mycompany.tqs.gohouse;

import javax.ejb.Singleton;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

/**
 *
 * @author Joao
 */
@ManagedBean(name = "beanAddUniversity", eager = true)
@Singleton
public class BeanAddUniversity {

    public String name;
    public String endereco;
    public String message;
    public String password; 

    private final DBHandler dBHandler = new DBHandler();

    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    
    /**
     * Empty constructor.
     */
    public BeanAddUniversity() {
        name = "";
        endereco = "";
        message = "";
        password = ""; 
    }

    /**
     * Submits a new University.
     */
    public void submitUniversity() {
        assert !"".equals(name) && !"".equals(endereco);

        if (CurrentUser.univ == null) {
            boolean added = dBHandler.addUniversity(password, name, endereco);
            if (added) {
                message = "Universidade Registada";
                //Adiciona como delegado da universidade
                dBHandler.getSingleUniversity(name).addDelegate(dBHandler.getSingleUser(CurrentUser.email));
                CurrentUser.univ = dBHandler.getSingleUniversity(name);
                showDialog();
                clearVars();
            } else {
                message = "Universidade não Registada. Verifique se o nome/endereço já existe";
                showDialog();

            }
        } else {
            message = "Um utilizador não pode ser delegado de 2 universidades";
            showDialog();
        }
    }

    /**
     * Show a message dialog.
     */
    private void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dlg1').show();");
    }

    private void clearVars() {
        this.endereco = "";
        this.message = "";
        this.name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
