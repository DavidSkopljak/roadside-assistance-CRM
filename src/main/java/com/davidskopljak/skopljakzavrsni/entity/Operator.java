package com.davidskopljak.skopljakzavrsni.entity;
//operators filling out the forms with client data and dispatching tow truck drivers
public class Operator extends Person{
    public Operator(Long id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public Operator(String firstName, String lastName) {
        super(firstName, lastName);
    }
}