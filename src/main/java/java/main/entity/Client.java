package java.main.entity;

//client calling in from the road
public class Client extends Person {
    private String contactNumber;

    public Client(Long id, String firstName, String lastName, String contactNumber) {
        super(id, firstName, lastName);
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}