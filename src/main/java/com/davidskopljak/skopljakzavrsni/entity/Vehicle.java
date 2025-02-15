package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;

import java.math.BigDecimal;
import java.time.LocalDate;

// vehicle, either for clients or tow truck drivers
// needs license plate, mileage(optional), date of first registration(necessary to decide if car is under warranty, optional), VehicleBrand, VehicleType
public class Vehicle extends Entity {
   private String licensePlate;
   private VehicleModel brand;
   private LocalDate firstRegistrationDate;
   private BigDecimal vin;

   public Vehicle(Long id, String licensePlate, VehicleModel brand, LocalDate firstRegistrationDate, BigDecimal vin) {
      super(id);
      this.licensePlate = licensePlate;
      this.brand = brand;
      this.firstRegistrationDate = firstRegistrationDate;
      this.vin = vin;
   }

   public Vehicle(String licensePlate, VehicleModel brand, LocalDate firstRegistrationDate, BigDecimal vin) {
      this.licensePlate = licensePlate;
      this.brand = brand;
      this.firstRegistrationDate = firstRegistrationDate;
      this.vin = vin;
   }

   public String getLicensePlate() {
      return licensePlate;
   }

   public VehicleModel getBrand() {
      return brand;
   }

   public LocalDate getFirstRegistrationDate() {
      return firstRegistrationDate;
   }

   public BigDecimal getVIN() {return vin;}
}
