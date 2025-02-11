package com.davidskopljak.skopljakzavrsni.entity;

import java.math.BigDecimal;

// needs fields for coordinates, operators note(to further specify location)
public class Location extends Entity {
    String address;
    String city;
    String country;
    String postalCode;
    BigDecimal coordinatesX;
    BigDecimal coordinatesY;

    public Location(Long id, String address, String city, String country, String postalCode, BigDecimal coordinatesX, BigDecimal coordinatesY) {
        super(id);
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
    }

    public Location(Long id, BigDecimal coordinatesX, BigDecimal coordinatesY) {
        super(id);
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
        setLocationDataFromCoordinates(coordinatesX, coordinatesY);
    }

    public Location(Long id, String address, String city, String country, String postalCode) {
        super(id);
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        setCoordinatesFromLocationData(country, city, postalCode, address);
    }

    private void setCoordinatesFromLocationData(String country, String city, String postalCode, String address) {
    }

    private void setLocationDataFromCoordinates(BigDecimal coordinatesX, BigDecimal coordinatesY) {
    }
}