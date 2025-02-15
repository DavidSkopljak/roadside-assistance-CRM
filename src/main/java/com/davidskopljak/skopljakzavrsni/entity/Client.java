package com.davidskopljak.skopljakzavrsni.entity;

//client calling in from the road
public class Client extends Person {
    private String contactNumber;

    public Client(Long id, String firstName, String lastName, String contactNumber) {
        super(id, firstName, lastName);
        this.contactNumber = contactNumber;
    }

    public Client(String firstName, String lastName, String contactNumber) {
        super(firstName, lastName);
        this.contactNumber = contactNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}