package com.mycompany.tqs.gohouse;

import dbclasses.PlatformUser;
import dbclasses.University;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import other.Utils;

/**
 *
 * @author joaos
 */
@ManagedBean(name = "loggingBean", eager = true)
@SessionScoped
public class LoggingBean implements Serializable {

    //Database handler
    private final DBHandler dBHandler = new DBHandler();
    //Used to render some Controls
    private boolean isLoggedIn = Utils.isLoggedIn();
    //Used to render if the user has a university.
    private boolean hasUniversity = Utils.hasUniversity();
    //Used to render button with text
    private University universityName;
    //User from Gsign in
    private String userName;
    //User Mail from GSignIn
    private String userMail;
    //User password
    private String password;
    //User
    private PlatformUser user;

    /**
     * Constructor. initialises Database Handler and verifies if user exists in
     * case of automated login.
     */
    public LoggingBean() {
        exists();
    }

    /**
     * Aquires user's name and email when user finishes signing in via Google
     * SignIn
     */
    public void userSignIn() {

        if (!exists()) {
            dBHandler.registerUser(password, userMail, userName, LocalDate.of(1997, 1, 1), false);
            exists();
        }

        //Redirect to HomePage
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            sessionMap.put("user", user);
            sessionMap.put("univ", universityName);

            FacesContext.getCurrentInstance().getExternalContext().redirect("faces/home.xhtml");
        } catch (IOException ex) {
            Logger.getLogger("Could not redirect.");
        }

    }

    /**
     * Verifies if a user exists, if so then it shall set all information to the
     * CurrentUser class.
     *
     * @return True if user exists, otherwise false
     */
    private boolean exists() {
        try {
            List<PlatformUser> d = dBHandler.getNMostPopularUsers(300);
            for (PlatformUser u : d) {
                if (u.getEmail().equals(userMail) && dBHandler.loginUser(userMail, password) && u.getId() != null) {
                    this.user = u;
                    if (u.getUniversity() != null) {
                        this.universityName = u.getUniversity();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.getLogger("Could not find ANY USER.");
            return false;
        }
        return false;
    }

    //userName getter
    public String getUserName() {
        return userName;
    }

    //userName setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //userMail getter
    public String getUserMail() {
        return userMail;
    }

    //userMail setter
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean isHasUniversity() {
        return hasUniversity;
    }

    public void setHasUniversity(boolean hasUniversity) {
        this.hasUniversity = hasUniversity;
    }

    public University getUniversityName() {
        return universityName;
    }

    public void setUniversityName(University universityName) {
        this.universityName = universityName;
    }

    public PlatformUser getUser() {
        return user;
    }

    public void setUser(PlatformUser user) {
        this.user = user;
    }


    

}
