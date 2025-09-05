package com.davidskopljak.skopljakzavrsni.entity;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private Long id = null;

    protected Entity(Long id) {
        this.id = id;
    }
    protected Entity() {this.id = null;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id = id;}
}
