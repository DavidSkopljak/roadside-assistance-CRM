package com.davidskopljak.skopljakzavrsni.entity;

public class Operator extends Person{
    private final String username;
    public Operator(Long id, String username,  String firstName, String lastName) {
        super(id, firstName, lastName);
        this.username = username;
    }

    public Operator(String username, String firstName, String lastName) {
        super(firstName, lastName);
        this.username = username;
    }

    public Operator(Long id, Operator operator){
        super(id, operator.getFirstName(), operator.getLastName());
        this.username = operator.getUsername();
    }

    public String getUsername() {
        return username;
    }
}