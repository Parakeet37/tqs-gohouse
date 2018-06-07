/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package other;

import dbclasses.University;

/**
 *
 * @author Joao
 */
public class CurrentUser {
    private static long id = -2;
    private static String email = "";
    private static boolean isUniversity = false;
    private static University univ = null;

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        CurrentUser.id = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CurrentUser.email = email;
    }

    public static boolean isIsUniversity() {
        return isUniversity;
    }

    public static void setIsUniversity(boolean isUniversity) {
        CurrentUser.isUniversity = isUniversity;
    }

    public static University getUniv() {
        return univ;
    }

    public static void setUniv(University univ) {
        CurrentUser.univ = univ;
    }
    
    
}
