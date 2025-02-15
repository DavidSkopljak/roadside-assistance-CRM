package com.davidskopljak.skopljakzavrsni.validation;

import java.math.BigDecimal;
import java.util.List;

public class Validators {
    private Validators() {}
    public static Boolean isValidString(String str){
        str = str.trim();
        return (!(str == null || str.isEmpty()) && (str.matches("[a-zA-ZčćđšžČĆĐŠŽ ]*")));
    }
    public static Boolean isValidRealNumber(String str){
        str = str.trim();
        return (!(str == null || str.isEmpty()) && (str.matches("-?\\d+(\\.\\d+)?")));
    }
    public static Boolean isValidInt(String str){
        str = str.trim();
        return (!(str == null || str.isEmpty()) && (str.matches("-?\\d+")));
    }
    public static Boolean isValidIntRange(String str, int min, int max){
        str = str.trim();
        return (!(str == null || str.isEmpty()) && (str.matches("-?\\d+")) && (Integer.parseInt(str) >= min && Integer.parseInt(str) <= max));
    }
    public static Boolean isValidRealNumberRange(String str, BigDecimal min, BigDecimal max){
        str = str.trim();
        return (!(str == null || str.isEmpty()) && (str.matches("-?\\d+(\\.\\d+)?")) && (new BigDecimal(str).compareTo(min) >= 0 && new BigDecimal(str).compareTo(max) <= 0));
    }

    public static Boolean isValidString(List<String> str){
        for(String s : str){
            if(Boolean.FALSE.equals(Validators.isValidString(s))){
                return false;
            }
        }
        return true;
    }

    public static Boolean isValidCROPhoneNumber(String str){
        str = str.trim();
        str = str.replaceAll("\\s", "");
        System.out.println(str);
        return (!(str == null || str.isEmpty()) && ((str.matches("^\\+385[1-9][0-9]{7,8}$")) || (str.matches("^0[1-9][0-9]{7,8}$"))));
    }

    public static Boolean isValidGeoCoords(String str){
        str = str.trim();
       return (!(str == null || str.isEmpty()) && (str.matches("-?[1-9][0-9]*(\\.[0-9]+)?,\\s*-?[1-9][0-9]*(\\.[0-9]+)?")));
    }

}
