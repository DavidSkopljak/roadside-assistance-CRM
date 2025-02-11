package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleBrand;

public record Workshop(Long id, String name, VehicleBrand vehicleBrand, Location location) {}
