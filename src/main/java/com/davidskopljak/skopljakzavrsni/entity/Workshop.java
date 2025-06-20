package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;

public record Workshop(Long id, String name, Location location, VehicleModel permittedVehicleModel) {
    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }
    public VehicleModel getPermittedVehicleModel() {return permittedVehicleModel;}
}

