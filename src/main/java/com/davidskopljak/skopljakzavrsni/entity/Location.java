package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.exceptions.ApiException;
import com.davidskopljak.skopljakzavrsni.exceptions.LocationNotFoundException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Location extends Entity implements Serializable {
    String address;
    String city;
    String country;
    String postalCode;
    BigDecimal latitude;
    BigDecimal longitude;

    public Location(Long id, String address, String city, String country, String postalCode, BigDecimal latitude, BigDecimal longitude) {
        super(id);
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Long id, BigDecimal latitude, BigDecimal longitude) {
        super(id);
        Location fetched = getLocationFromCoordinates(latitude, longitude);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.latitude = fetched.latitude;
        this.longitude = fetched.longitude;
    }

    public Location(Long id, String address, String city, String country, String postalCode) {
        super(id);
        Location fetched = getLocationFromAddress(country, city, postalCode, address);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.latitude = fetched.latitude;
        this.longitude = fetched.longitude;
    }

    public Location(String address, String city, String country, String postalCode, BigDecimal latitude, BigDecimal longitude) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(BigDecimal latitude, BigDecimal longitude) {
        Location fetched = getLocationFromCoordinates(latitude, longitude);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.latitude = fetched.latitude;
        this.longitude = fetched.longitude;
    }

    public Location(String postalCode, String country, String city, String address) {
        Location fetched = getLocationFromAddress(country, city, postalCode, address);

        this.address = fetched.address;
        this.city = fetched.city;
        this.country = fetched.country;
        this.postalCode = fetched.postalCode;
        this.latitude = fetched.latitude;
        this.longitude = fetched.longitude;
    }
    
    public Location(Long id, Location location){
        super(id);
        this.address = location.address;
        this.city = location.city;
        this.country = location.country;
        this.postalCode = location.postalCode;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public static Location getLocationFromAddress(String country, String city, String postalCode, String address) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            String rawQuery = String.join(" ", address, city, postalCode, country);
            String encodedQuery = URLEncoder.encode(rawQuery, StandardCharsets.UTF_8);

            String fullUrl = "https://nominatim.openstreetmap.org/search?addressdetails=1&q=" + encodedQuery + "&format=jsonv2&limit=1";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .build();


            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Object parsed = JSONValue.parse(response.body());
            JSONArray results = (JSONArray) parsed;

            if (results == null || results.isEmpty()) {
                throw new LocationNotFoundException("Could not translate address to coordinates. Please check your address and try again.");
            }

            JSONObject jsonObjectdecode = (JSONObject) results.getFirst();

            String lat = (String) jsonObjectdecode.get("lat");
            String lon = (String) jsonObjectdecode.get("lon");

            if(lat == null || lat.isEmpty() || lon == null || lon.isEmpty()){
                throw new LocationNotFoundException("No coordinates retrieved for queried location. Please check your address and try again.");
            }

            return new Location(address, city, country, postalCode, new BigDecimal(lat), new BigDecimal(lon));
        } catch (IOException e) {
            throw new ApiException("Error translating address data to coordinates: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Thread was interrupted while translating address data to coordinates:" + e.getMessage());
        }
    }

    public static Location getLocationFromCoordinates(BigDecimal lat, BigDecimal lon) {
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://nominatim.openstreetmap.org/reverse?lat="+ lat +"&lon="+ lon + "&format=jsonv2"))
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
            throw new ApiException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}