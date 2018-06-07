package other;

/**
 *
 * @author Joao
 */
public class Utils {
  
    /**
     * Checks if it is logged in.
     * @return True if User is logged in, otherwise returns false.
     */
    public static boolean isLoggedIn(){
        return !"".equals(CurrentUser.getEmail());
    }
    /**
     * Checks if it has a university associated.
     * @return True if it has, otherwise False.
     */
    public static boolean hasUniversity(){
        return CurrentUser.getUniv()!=null;
    }
    
    /**
     * Gets the university name.
     * @return University name.
     */
    public static String universityName(){
        if(CurrentUser.getUniv() == null || "".equals(CurrentUser.getUniv().getName()))
            return "";
        return CurrentUser.getUniv().getName();
    }
    
}
