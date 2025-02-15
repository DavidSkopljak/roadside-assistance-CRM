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

    public Location(String address, String city, String country, String postalCode, BigDecimal coordinatesX, BigDecimal coordinatesY) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
    }

    public Location(BigDecimal coordinatesX, BigDecimal coordinatesY) {
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
        setLocationDataFromCoordinates(coordinatesX, coordinatesY);
    }

    public Location(String postalCode, String country, String city, String address) {
        this.postalCode = postalCode;
        this.country = country;
        this.city = city;
        this.address = address;
        setCoordinatesFromLocationData(country, city, postalCode, address);
    }

    private void setCoordinatesFromLocationData(String country, String city, String postalCode, String address) {
    }

    private void setLocationDataFromCoordinates(BigDecimal coordinatesX, BigDecimal coordinatesY) {
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public BigDecimal getCoordinatesX() {
        return coordinatesX;
    }

    public BigDecimal getCoordinatesY() {
        return coordinatesY;
    }
}