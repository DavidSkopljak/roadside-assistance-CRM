package com.davidskopljak.skopljakzavrsni.entity;

import java.io.Serializable;

public class Client extends Person implements Serializable {
    private String contactNumber;

    public Client(Long id, String firstName, String lastName, String contactNumber) {
        super(id, firstName, lastName);
        this.contactNumber = contactNumber;
    }

    public Client(String firstName, String lastName, String contactNumber) {
        super(firstName, lastName);
        this.contactNumber = contactNumber;
    }

    public Client(Long id, Client client) {
        super(id, client.getFirstName(), client.getLastName());
        this.contactNumber = client.getContactNumber();
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}