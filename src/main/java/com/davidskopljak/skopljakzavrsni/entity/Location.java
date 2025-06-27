package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.exceptions.ApiException;
import com.davidskopljak.skopljakzavrsni.exceptions.LocationNotFoundException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
        Location fetched = getLocationFromCoordinates(coordinatesX, coordinatesY);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.coordinatesX = fetched.coordinatesX;
        this.coordinatesY = fetched.coordinatesY;
    }

    public Location(Long id, String address, String city, String country, String postalCode) {
        super(id);
        Location fetched = getLocationFromAddress(country, city, postalCode, address);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.coordinatesX = fetched.coordinatesX;
        this.coordinatesY = fetched.coordinatesY;
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
        Location fetched = getLocationFromCoordinates(coordinatesX, coordinatesY);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.coordinatesX = fetched.coordinatesX;
        this.coordinatesY = fetched.coordinatesY;
    }

    public Location(String postalCode, String country, String city, String address) {
        Location fetched = getLocationFromAddress(country, city, postalCode, address);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.coordinatesX = fetched.coordinatesX;
        this.coordinatesY = fetched.coordinatesY;
    }

    public static Location getLocationFromAddress(String country, String city, String postalCode, String address) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String addressData = address + "+" + postalCode + "+" + city + "+" + country;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://nominatim.openstreetmap.org/search?addressdetails=1&q=" + addressData + "&format=jsonv2&limit=1"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Object file = JSONValue.parse(response.body());
            JSONObject jsonObjectdecode = (JSONObject) file;

            String lat = (String) jsonObjectdecode.get("lat");
            String lon = (String) jsonObjectdecode.get("lon");

            if(lat == null || lat.isEmpty() || lon == null || lon.isEmpty()){
                throw new LocationNotFoundException("Could not translate address to coordinates. Please check your address and try again.");
            }

            return new Location(address, city, country, postalCode, new BigDecimal(lat), new BigDecimal(lon));
        } catch (IOException e) {
            throw new ApiException("Error translating address data to coordinates: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new ApiException("Thread was interrupted while translating address data to coordinates:" + e.getMessage());
        }
    }

    public static Location getLocationFromCoordinates(BigDecimal lat, BigDecimal lon) {
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat="+ lat +"&lon="+ lon))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Object file = JSONValue.parse(response.body());
            JSONObject jsonObjectdecode = (JSONObject) file;

            JSONObject addressObject = (JSONObject) jsonObjectdecode.get("address");

            if(addressObject == null || addressObject.isEmpty()){
                throw new LocationNotFoundException("Could not translate coordinates to address. Please check your coordinates and try again.");
            }

            String houseNumber = (String) addressObject.get("house_number");
            String road = (String) addressObject.get("road");
            String postcode = (String) addressObject.get("postcode");
            String country = (String) addressObject.get("country");
            String address;
            String town;
            if(addressObject.get("village") != null) {
                town = (String) addressObject.get("village");
            }else if(addressObject.get("town") != null){
                town = (String) addressObject.get("town");
            }else if(addressObject.get("city") != null){
                town = (String) addressObject.get("city");
            }else{
                town = "Unknown";
            }

            if(houseNumber != null && !houseNumber.isEmpty()){
                address = road + " " +  houseNumber;
            }else{
                address = road;
            }

            return new Location(address, town, country, postcode, lat, lon);
        } catch (IOException e) {
            throw new ApiException("Error translating address data to coordinates: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new ApiException("Thread was interrupted while translating address data to coordinates:" + e.getMessage());
        }
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