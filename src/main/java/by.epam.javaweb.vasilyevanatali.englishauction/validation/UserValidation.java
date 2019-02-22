package main.java.by.epam.javaweb.vasilyevanatali.englishauction.validation;

import main.java.by.epam.javaweb.vasilyevanatali.englishauction.entity.User;

import java.util.regex.Pattern;

public class UserValidation {
    private static final String USER_LOGIN = "^[a-zA-Z][a-zA-Z0-9-_\\.]{1,20}$";
    private static final String USER_PASS = "(?=^.{8,50}$)((?=.*\\d|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*)$";
    private static final String USER_EMAIL = "^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$";
    private static final String INTEGER_NUMBER = "^[1-9]\\d{0,19}$";
    private static final int ADMIN_ROLE = 1;
    private static final int USER_ROLE = 2;

    public static boolean loginValidator(String  enterLogin, String enterPass){
        return Pattern.matches(USER_LOGIN, enterLogin)&& Pattern.matches(USER_PASS, enterPass);
    }

    public static boolean registrationValidator(String  enterLogin, String enterPass, String enterEmail){
        return loginValidator(enterLogin, enterPass) && Pattern.matches(USER_EMAIL, enterEmail);
    }

    public static boolean changeRoleValidator (String userId, String newRoleId){
        return Pattern.matches(INTEGER_NUMBER, userId) && Pattern.matches(INTEGER_NUMBER, newRoleId);
    }

    public static boolean hasRoleAdmin(Object role){
        return (role!=null) && (((User)role).getRole().ordinal()==ADMIN_ROLE);
    }

    public static boolean hasRoleAdminOrUser(Object role){
        return (role!=null) && ((((User)role).getRole().ordinal()==ADMIN_ROLE) ||
                (((User)role).getRole().ordinal()==USER_ROLE));
    }
}
