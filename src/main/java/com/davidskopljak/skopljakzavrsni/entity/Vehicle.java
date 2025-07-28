package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;

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

   public Vehicle(Long id, Vehicle vehicle){
      super(id);
      this.licensePlate = vehicle.getLicensePlate();
      this.model = vehicle.getModel();
      this.vin = vehicle.getVin();
   }

   public String getLicensePlate() {
      return licensePlate;
   }

   public VehicleModel getModel() {
      return model;
   }

   public String getVin() {return vin;}
}
