package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import java.time.LocalDate;

// vehicle, either for clients or tow truck drivers
// needs license plate, mileage(optional), date of first registration(necessary to decide if car is under warranty, optional), VehicleBrand, VehicleType
public class Vehicle extends Entity {
   private String licensePlate;
   private VehicleModel model;
   private LocalDate firstRegistrationDate;
   private String vin;

   public Vehicle(Long id, String licensePlate, VehicleModel model, LocalDate firstRegistrationDate, String vin) {
      super(id);
      this.licensePlate = licensePlate;
      this.model = model;
      this.firstRegistrationDate = firstRegistrationDate;
      this.vin = vin;
   }

   public Vehicle(String licensePlate, VehicleModel model, LocalDate firstRegistrationDate, String vin) {
      this.licensePlate = licensePlate;
      this.model = model;
      this.firstRegistrationDate = firstRegistrationDate;
      this.vin = vin;
   }

   public String getLicensePlate() {
      return licensePlate;
   }

   public VehicleModel getModel() {
      return model;
   }

   public LocalDate getFirstRegistrationDate() {
      return firstRegistrationDate;
   }

   public String getVin() {return vin;}
}
