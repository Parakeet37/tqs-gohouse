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

import dbclasses.PlatformUser;
import dbclasses.Property;
import dbclasses.PropertyType;
import dbclasses.Room;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "searchBean", eager = true)
@SessionScoped
public class BeanSearch implements Serializable{
    private String searchValue;
    private List<Property> allProperties = new ArrayList<>();
    private List<Property> searchResults = new ArrayList<>();
    
   @PostConstruct
   public void construct(){
       setSearchValue("");
       
       Set<Room> rs = new HashSet<>();
       rs.add(new Room("awawa", 0, null));
       rs.add(new Room("2323123", 0, null));
       
       PlatformUser pu = new PlatformUser("myEmail@Email", "Jackson", LocalDate.now(), true);
       
       Property p = new Property(pu, 12f, 15f, "wawa", PropertyType.HOUSE, 'a', 0, rs);
       Property p2 = new Property(pu, 3.3f, 12f, "wawa", PropertyType.HOUSE, 'a', 0, rs);
       Property p3 = new Property(pu, -18f, 17f, "ewe", PropertyType.HOUSE, 'a', 0, rs);
       
       List<Property> temp = new ArrayList<>();
       temp.add(p);temp.add(p2);temp.add(p3);
       
        setAllProperties(temp);
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
    
    public void redirectTo(long id) throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("/tqs-gohouse-1.0-SNAPSHOT/faces/propertyDetailsPage.xhtml?propertyId="+id);

        
    }

    public String searchProperties() throws IOException{
        searchResults.clear();
        List<Property> temp = new ArrayList<>();
    
        for(Property p: allProperties){
            if(p.getAddress().contains(searchValue) || searchValue.equals("")){
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