/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

/**
 * Object received via JSON POST.
 * @author demo
 */
public class POSTUser {
    private String email;
    private String name;
    private String password;
    private boolean isDelegate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsDelegate() {
        return isDelegate;
    }

    public void setIsDelegate(boolean isDelegate) {
        this.isDelegate = isDelegate;
    }
    
    
}
