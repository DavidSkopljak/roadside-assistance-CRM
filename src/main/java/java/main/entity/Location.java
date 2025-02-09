package java.main.entity;

// needs fields for coordinates, operators note(to further specify location)
public class Location extends Entity {
    private String address;
    private String city;
    private String country;
    private String postalCode;
    private String coordinatesX;
    private String coordinatesY;

    public Location(Long id, String address, String city, String country, String postalCode, String coordinatesX, String coordinatesY) {
        super(id);
        this.address = address;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.coordinatesX = coordinatesX;
        this.coordinatesY = coordinatesY;
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

    public String getCoordinatesX() {
        return coordinatesX;
    }

    public String getCoordinatesY() {
        return coordinatesY;
    }
}
