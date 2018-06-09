/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

import dbclasses.PlatformUser;
import dbclasses.University;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author joaos
 */
public class Utils {

    public static boolean isLoggedIn() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        return sessionMap.get("user") != null;
    }

    public static boolean hasUniversity() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        return sessionMap.get("univ") != null;
    }

    public static String universityName() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        University uni = (University) sessionMap.get("univ");
        if (uni == null) {
            return "";
        }
        return uni.getName();
    }

    public static University userUniversity() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        University uni = (University) sessionMap.get("univ");
        return uni;
    }

    public static long getUserId() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        PlatformUser us = (PlatformUser) sessionMap.get("user");
        if (us == null) {
            return -1;
        }
        return us.getId();
    }

    public static String getUserEmail() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        PlatformUser us = (PlatformUser) sessionMap.get("user");
        return us.getEmail();
    }

}
