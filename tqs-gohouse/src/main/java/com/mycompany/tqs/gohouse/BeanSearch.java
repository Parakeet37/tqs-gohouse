/*
 * To change this license header, choose License Headers in Project AllProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tqs.gohouse;

/**
 *
 * @author demo
 */

import dbclasses.Property;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "searchBean", eager = true)
@SessionScoped
public class BeanSearch implements Serializable{
    private final DBHandler dbHandler = new DBHandler();
    
    private String searchValue;
    private List<Property> allProperties = new ArrayList<>();
    private List<Property> searchResults = new ArrayList<>();
    
   @PostConstruct
   public void construct(){
       List<Property> allProperties = dbHandler.getUnverifiedProperties();
       allProperties.addAll(dbHandler.getVerifiedProperties());
        setAllProperties(allProperties);
        setSearchResults(allProperties);
   }

    public List<Property> getAllProperties() {
        return allProperties;
    }

    public void setAllProperties(List<Property> allProperties) {
        this.allProperties = allProperties;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public List<Property> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<Property> searchResults) {
        this.searchResults = searchResults;
    }
    
    

    public String searchProperties() throws IOException{
        searchResults.clear();
        List<Property> temp = new ArrayList<>();
    
        for(Property p: allProperties){
            if(p.getAddress().contains(searchValue)){
                System.out.println(p.getAddress());
                temp.add(p);
            }
        }
        setSearchResults(temp);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        return "search.xhtml";
    }
    
    

}