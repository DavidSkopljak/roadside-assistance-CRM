package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;

import java.io.Serializable;

public record Workshop(Long id, String name, Location location, VehicleModel permittedVehicleModel) implements Serializable {
    public Workshop(Long id, Workshop workshop){
        this(id, workshop.getName(), workshop.getLocation(), workshop.getPermittedVehicleModel());
    }

    public Workshop(String name, Location location, VehicleModel permittedVehicleModel) {
        this(null, name, location, permittedVehicleModel);
    }


    public Long getId() {return id;}
    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }
    public VehicleModel getPermittedVehicleModel() {return permittedVehicleModel;}
}

