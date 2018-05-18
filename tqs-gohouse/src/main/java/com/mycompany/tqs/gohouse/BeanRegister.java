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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "registerBean", eager = true)
@SessionScoped
public class BeanRegister implements Serializable{


    private String inputUName;
    private String inputPassword;
    private String inputName;
    private String inputEmail;
    private boolean isStudent;
    private String errorPrompt;
   
   @PostConstruct
   public void construct(){
        setInputUName("I am here now");
        setInputPassword("");
        setInputName("");
        setInputEmail("");
        setIsStudent(false);
        setErrorPrompt("");
   }

    
    public String getInputUName() {
        return inputUName;
    }

    public void setInputUName(String inputUName) {
        this.inputUName = inputUName;
    }

    public String getInputPassword() {
        return inputPassword;
    }

    public void setInputPassword(String inputPassword) {
        this.inputPassword = inputPassword;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }

    public boolean isIsStudent() {
        return isStudent;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }

    public String getErrorPrompt() {
        return errorPrompt;
    }

    public void setErrorPrompt(String errorPrompt) {
        this.errorPrompt = errorPrompt;
    }
    

    public String registerCommand(){
        if(!inputUName.matches("[a-zA-Z0-9]{3,20}"))
            setErrorPrompt("Username must be alfanumeric between 3 to 20 characters");
        else if(!inputPassword.matches("[a-zA-Z0-9]{6,30}"))
            setErrorPrompt("Password must be alfanumeric between 3 to 20 characters");
        else
            setErrorPrompt("Tudo ok.");
        return "registerAccount.xhtml";
    }
}