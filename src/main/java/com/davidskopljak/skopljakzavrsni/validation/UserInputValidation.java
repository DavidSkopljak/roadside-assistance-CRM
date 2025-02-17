package com.davidskopljak.skopljakzavrsni.validation;

import com.davidskopljak.skopljakzavrsni.entity.Client;
import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.entity.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class UserInputValidation {
    public static Boolean validatePerson(String firstName, String lastName, String contactNumber) {
        Boolean validFirstName = Validators.isValidString(firstName);
        Boolean validLastName = Validators.isValidString(lastName);
        Boolean validContactNumber = Validators.isValidHRPhoneNumber(contactNumber);
        return (validFirstName && validLastName && validContactNumber);
    }

    public static Boolean validateClient(Client client) {
        Boolean validFirstName = Validators.isValidString(client.getFirstName());
        Boolean validLastName = Validators.isValidString(client.getLastName());
        Boolean validContactNumber = Validators.isValidHRPhoneNumber(client.getContactNumber());
        return (validFirstName && validLastName && validContactNumber);
    }

    public static Boolean validateDriver(Driver driver) {
        Boolean validFirstName = Validators.isValidString(driver.getFirstName());
        Boolean validLastName = Validators.isValidString(driver.getLastName());
        Boolean validContactNumber = Validators.isValidHRPhoneNumber(driver.getContactNumber());
        return (validFirstName && validLastName && validContactNumber);
    }



    public static Boolean validatePerson(String firstName, String lastName) {
        Boolean validFirstName = Validators.isValidString(firstName);
        Boolean validLastName = Validators.isValidString(lastName);
        return (validFirstName && validLastName);
    }

    public static Boolean validateLocation(Location location) {
        Boolean validCoords = Validators.isValidGeoCoords(location.getCoordinatesX().toString() + ", " + location.getCoordinatesY().toString());
        Boolean validAddress = Validators.isValidString(Arrays.asList(location.getCountry(), location.getCity(), location.getPostalCode(), location.getAddress()));
        return (validCoords && validAddress);
    }
}