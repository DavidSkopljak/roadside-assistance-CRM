package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;

public record Workshop(Long id, String name, VehicleModel vehicleModel, Location location) {}

