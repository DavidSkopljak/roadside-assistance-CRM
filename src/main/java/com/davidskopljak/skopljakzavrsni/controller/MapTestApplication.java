package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MapTestApplication extends Application {
    private final MapView mapView = new MapView();
    Location location;

    @Override
    public void start(Stage stage) {
        mapView.setMapType(MapType.OSM);
        mapView.initialize(Configuration.builder()
                .showZoomControls(true)
                .build());
        mapView.setCenter(new Coordinate(45.8150, 15.9819));
        mapView.setZoom(13);
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            Coordinate coord = event.getCoordinate();
            location = Location.getLocationFromCoordinates(BigDecimal.valueOf(coord.getLatitude()), BigDecimal.valueOf(coord.getLongitude()));
            System.out.println(location.getAddress() + " " + location.getCity() + " " + location.getCountry() + " " + " " + location.getPostalCode() + " " + location.getLatitude() + " " + location.getLongitude() + "\n----------------------------");

            Marker marker = Marker.createProvided(Marker.Provided.RED)
                    .setPosition(coord)
                    .setVisible(true);
            mapView.addMarker(marker);
        });

        BorderPane root = new BorderPane(mapView);
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("MapJFX - Basic Example");
        stage.show();




    }

    private void coordsToAddress(String lat, String lon){
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat="+ lat +"&lon="+ lon))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Object file = JSONValue.parse(response.body());
            System.out.println(response.body());
            JSONObject jsonObjectdecode = (JSONObject) file;

            JSONObject addressObject = (JSONObject) jsonObjectdecode.get("address");

            String houseNumber = (String) addressObject.get("house_number");
            String road = (String) addressObject.get("road");
            String town = (String) addressObject.get("town");
            String postcode = (String) addressObject.get("postcode");
            String country = (String) addressObject.get("country");


            String location = houseNumber + "+" + road + "+" + town + "+" + postcode + "+" + country;
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create("https://nominatim.openstreetmap.org/search?addressdetails=1&q=" + location + "&format=jsonv2&limit=1"))
                    .build();

            HttpResponse<String> response2 = client.send(request2,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response2.body());

        }catch(IOException | InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
