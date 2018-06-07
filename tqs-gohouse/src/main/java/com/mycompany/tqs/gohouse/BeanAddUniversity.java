package com.mycompany.tqs.gohouse;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import other.CurrentUser;
import other.Utils;

/**
 *
 * @author Joao
 */
@ManagedBean(name = "beanAddUniversity", eager = true)
@ViewScoped
public class BeanAddUniversity {

    private String name;
    private String endereco;
    private String message;
    private String password;
    private boolean addedUniv = true;
    //Database handler
    private final DBHandler dBHandler = new DBHandler();

    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();

    /**
     * Empty constructor that cleans the variables.
     */
    public BeanAddUniversity() {
        name = "";
        endereco = "";
        message = "";

    }

    /**
     * Submits a new University.
     */
    public void submitUniversity() {
        assert !"".equals(name) && !"".equals(endereco);

        if (CurrentUser.univ == null) {
            boolean added = dBHandler.addUniversity(name, endereco, password);
            if (added) {

                addedUniv = true;
                //Adiciona como delegado da universidade
                dBHandler.getSingleUniversity(name).addDelegate(dBHandler.getSingleUser(CurrentUser.email));
                CurrentUser.univ = dBHandler.getSingleUniversity(name);
                message = "Universidade Registada com sucesso.";
                showDialog();
                clearVars();
            } else {
                message = "Universidade não Registada. Verifique se o nome/endereço já existe";
                addedUniv = false;
                showDialog();

            }
        } else {
            message = "Não é possivel ser delegado de 2 ou mais Universidades.";
            addedUniv = false;
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
     * Clears all variables.
     */
    private void clearVars() {
        this.endereco = "";
        this.message = "";
        this.name = "";
    }

    // Getters and setters
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAddedUniv() {
        return addedUniv;
    }

    public void setAddedUniv(boolean addedUniv) {
        this.addedUniv = addedUniv;
    }

}
