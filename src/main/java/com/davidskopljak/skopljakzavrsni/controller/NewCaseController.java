package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Client;
import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.davidskopljak.skopljakzavrsni.entity.Operator;

import java.math.BigDecimal;

public class NewCaseController {
    private Client client;
    private Operator operator;
    private Location location;

    public Boolean validateCaseData() {
        return true;
    }

    public void setClient(String firstName, String lastName, String contactNumber){
        this.client = new Client(firstName, lastName, contactNumber);
    }
    public void setOperator(String firstName, String lastName){
        this.operator = new Operator(firstName, lastName);
    }
    public void setLocation(BigDecimal xCoord, BigDecimal yCoord){
        this.location = new Location(xCoord, yCoord);
    }
    public void setLocation(String postalCode, String country, String city, String address){
        this.location = new Location(postalCode, country, city, address);
    }
}
