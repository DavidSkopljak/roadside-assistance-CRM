package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import java.time.LocalDate;

// vehicle, either for clients or tow truck drivers
// needs license plate, mileage(optional), date of first registration(necessary to decide if car is under warranty, optional), VehicleBrand, VehicleType
public class Vehicle extends Entity {
   private String licensePlate;
   private VehicleModel model;
   private String vin;

   public Vehicle(Long id, String licensePlate, VehicleModel model, String vin) {
      super(id);
      this.licensePlate = licensePlate;
      this.model = model;
      this.vin = vin;
   }

   public Vehicle(String licensePlate, VehicleModel model, String vin) {
      this.licensePlate = licensePlate;
      this.model = model;
      this.vin = vin;
   }

   public String getLicensePlate() {
      return licensePlate;
   }

   public VehicleModel getModel() {
      return model;
   }

   public String getVin() {return vin;}
}
