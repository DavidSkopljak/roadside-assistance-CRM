package com.davidskopljak.skopljakzavrsni.validation;

import java.util.List;

public class Validators {
    private Validators() {}
    public static Boolean isValidString(String str){
        str = str.trim();
        return (!(str.isEmpty()) && (str.matches("(?iu)[A-ZČĆŠŽĐ ]*")));
    }

    public static Boolean isValidInt(String str){
        str = str.trim();
        return (!(str.isEmpty()) && (str.matches("-?\\d+")));
    }

    public static Boolean isValidString(List<String> str){
        for(String s : str){
            if(Boolean.FALSE.equals(Validators.isValidString(s))){
                return false;
            }
        }
        return true;
    }

    public static Boolean isValidHRPhoneNumber(String str){
        str = str.trim();
        str = str.replaceAll("\\s", "");
        return (!(str.isEmpty()) && ((str.matches("^\\+385[1-9]\\d{7,8}$")) || (str.matches("^0[1-9]\\d{7,8}$")) || (str.matches("^0{2}385[1-9]\\d{7,8}$"))));
    }

    public static Boolean isValidGeoCoords(String str){
        str = str.trim();
        return (!(str.isEmpty()) && (str.matches("-?[1-9]\\d*(\\.\\d+)?,\\s*-?[1-9]\\d*(\\.\\d+)?")));
    }

    public static Boolean isValidHRLicensePlateNumber(String str){
        str = str.trim();
        str = str.replaceAll("\\s", "");
        str = str.replace("-", "");
        return (!(str.isEmpty()) && (str.matches("(?iu)^[A-ZŠČĆŽĐ]{2}\\d{3,4}[A-ZŠČĆŽĐ]{1,2}$")));
    }

    public static Boolean isValidAddress(String str){
        str = str.trim();
        str = str.replace("\\s", "");
        return !(str.isEmpty()) && str.matches("(?iu)[A-ZČĆŠŽĐ0-9  /]*");
    }

    public static Boolean isValidVIN(String str){
        str = str.trim();
        str = str.replace("\\s", "");
        return (!(str.isEmpty()) && (str.matches("(?iu)^[A-HJ-NPR-Z\\d]{17}$")));
    }
    
    public static String removeWhitespace(String str){
        return str.replaceAll("\\s", "");
    }

    public static String removeSpecialCharacters(String str){
       return str.replaceAll("(?iu)[^A-Z0-9ŠĐČĆŽ]", "");
    }

    public static Boolean isNotNull(Object object){
        return(object != null);
    }

    public static Boolean isNotNull(List<Object> objects){
        for(Object object : objects){
            if(object == null){
                return false;
            }
        }
        return true;
    }
}
