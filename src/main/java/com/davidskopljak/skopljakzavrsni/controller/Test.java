package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.validation.Validators;

import java.math.BigDecimal;

public class Test {
    public static void main(String[] args) {
        System.out.println(Validators.isValidString(""));
        System.out.println(Validators.isValidString("beeboop"));
        System.out.println(Validators.isValidString("5123ffagsfd"));
        System.out.println(Validators.isValidString(" "));
        System.out.println(Validators.isValidString("đuro đurđimirević1"));
        System.out.println();
        System.out.println(Validators.isValidInt("15"));
        System.out.println(Validators.isValidInt("-15"));
        System.out.println(Validators.isValidInt(" 15 "));
        System.out.println(Validators.isValidInt("-15 "));
        System.out.println(Validators.isValidInt("15.43"));
        System.out.println();
        System.out.println(Validators.isValidRealNumber("-15đ"));
        System.out.println();
        System.out.println(Validators.isValidRealNumberInRange("-2.43", new BigDecimal(-5), new BigDecimal(-2)));
        System.out.println();
        System.out.println(Validators.isValidHRPhoneNumber("   0 9 5  8340817"));
        System.out.println();
        System.out.println(Validators.isValidGeoCoords("64.95981292849561 41.90663539262211"));
    }
}
