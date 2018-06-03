package com.mycompany.tqs.gohouse;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Joao
 */
@ManagedBean(name = "beanAddUniversity", eager = true)
@SessionScoped
public class BeanAddUniversity {

    public String name;
    public String endereco;
    public String message;

    private final DBHandler dBHandler = new DBHandler();

    /**
     * Empty constructor.
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

        boolean added = dBHandler.addUniversity(name, endereco);
        if (added) {
            message = "Universidade Registada";
            showDialog();
            clearVars();
        } else {
            message = "Universidade não Registada. Verifique se o nome/endereço já existe";
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

    private void clearVars(){
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

}
