package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleBrand;

import java.time.LocalDate;

// vehicle, either for clients or tow truck drivers
// needs license plate, mileage(optional), date of first registration(necessary to decide if car is under warranty, optional), VehicleBrand, VehicleType
public class Vehicle extends Entity {
   private String licensePlate;
   private VehicleBrand brand;
   private LocalDate firstRegistrationDate;

   public Vehicle(Long id, String licensePlate, VehicleBrand brand, LocalDate firstRegistrationDate) {
      super(id);
      this.licensePlate = licensePlate;
      this.brand = brand;
      this.firstRegistrationDate = firstRegistrationDate;
   }

   public String getLicensePlate() {
      return licensePlate;
   }

   public VehicleBrand getBrand() {
      return brand;
   }

   public LocalDate getFirstRegistrationDate() {
      return firstRegistrationDate;
   }
}
