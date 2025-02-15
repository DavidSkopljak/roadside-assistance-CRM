package com.davidskopljak.skopljakzavrsni.entity;

public abstract class Entity {
    private Long id;

    protected Entity(Long id) {
        this.id = id;
    }
    protected Entity() {this.id = -1L;}//temp id - means object was not saved to db yet

    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id = id;}
}
