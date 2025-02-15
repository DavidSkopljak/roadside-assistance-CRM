package com.davidskopljak.skopljakzavrsni.entity;

public abstract class Entity {
    private Long id;

    protected Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
