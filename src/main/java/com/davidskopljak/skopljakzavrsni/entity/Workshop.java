package com.davidskopljak.skopljakzavrsni.entity;

public record Workshop(Long id, String name, Location location) {
    public Workshop(String name, Location location){
        this(null, name, location);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}

