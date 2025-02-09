package java.main.entity;

import java.main.enums.VehicleBrand;
import java.time.LocalDate;

// vehicle, either for clients or tow truck drivers
// needs license plate, mileage(optional), date of first registration(necessary to decide if car is under warranty, optional), VehicleBrand, VehicleType
public class Vehicle {
   private Long id;
   private String licensePlate;
   private VehicleBrand brand;
   private LocalDate firstRegistrationDate;
}
