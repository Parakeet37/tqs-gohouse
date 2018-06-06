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
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import other.Utils;

@ManagedBean(name = "searchBean", eager = true)
@SessionScoped
public class BeanSearch implements Serializable {

    //Database handler
    private final DBHandler dBHandler = new DBHandler();

    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    //Value used to search properties
    private String searchValue;
    //List with all properties. Used when the search is empty
    private List<Property> allProperties = new ArrayList<>();
    //List with properties with search value
    private List<Property> searchResults = new ArrayList<>();

    /**
     * Post constructor that populates all lists with properties.
     */
    @PostConstruct
    public void construct() {
        List<Property> allProps = dBHandler.getUnverifiedProperties();
        allProps.addAll(dBHandler.getVerifiedProperties());
        setAllProperties(allProps);
        setSearchResults(allProps);
    }

    /**
     * Searched from all properties the ones with given search parameter.
     *
     * @return Same page refreshed.
     * @throws IOException Couldn't refresh page.
     */
    public String searchProperties() throws IOException {
        searchResults.clear();
        List<Property> temp = new ArrayList<>();

        for (Property p : allProperties) {
            if (p.getAddress().contains(searchValue)) {
                Logger.getLogger(p.getAddress());
                temp.add(p);
            }
        }
        setSearchResults(temp);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        return "search.xhtml";
    }

    //Getters and setters
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

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

}
