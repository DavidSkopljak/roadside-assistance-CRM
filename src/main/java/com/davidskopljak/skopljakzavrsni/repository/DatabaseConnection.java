package com.davidskopljak.skopljakzavrsni.repository;
import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.enums.ServiceState;
import com.davidskopljak.skopljakzavrsni.enums.ServiceType;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class DatabaseConnection {
    public static void main(String[] args) throws IOException, SQLException {
        /*VehicleRepository vehicleRepository = new VehicleRepository();
        try{
            Long vehicleId = vehicleRepository.save(new Vehicle("TESTPLATE2", VehicleModel.BMW, null, "11111111111111112"));
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            List<Vehicle> vehicles = vehicleRepository.findAll();
            for (Vehicle vehicle : vehicles) {

            }
        }catch (SQLException e){
            e.printStackTrace();
        }*/

        /*OperatorRepository operatorRepository = new OperatorRepository();
        Long id = operatorRepository.save(new Operator("marko", "marulic"));
        System.out.println(id);*/

        /*DriverRepository driverRepository = new DriverRepository();
        Location location = new Location("address", "city", "country", "postalCode", new BigDecimal(45.8089772239981), new BigDecimal(15.716704654770382));
        Vehicle vehicle = new Vehicle("licensePlate", VehicleModel.CHERY, null, "11111111111111111");
        Long id = driverRepository.save(new Driver("vozko", "vojkovic", "192", location, vehicle, DriverState.AVAILABLE));

        System.out.println("saved driver(id " + id + ")");

        List<Driver> drivers = driverRepository.findAll();
        for (Driver driver : drivers) {
            System.out.println(driver.getState());
            System.out.println(driver.getFirstName());
            System.out.println(driver.getLastName());
            System.out.println(driver.getVehicle().getModel());
            System.out.println(driver.getContactNumber());
            System.out.println(driver.getCurrentLocation().getCoordinatesX() + ", " + driver.getCurrentLocation().getCoordinatesY());
            System.out.println();
        }*/

        /*
        ServiceRepository serviceRepository = new ServiceRepository();
        Location location = new Location("address", "city", "country", "postalCode", new BigDecimal(45.8089772239981), new BigDecimal(15.716704654770382));
        Vehicle vehicle = new Vehicle("licensePlate", VehicleModel.CHERY, null, "11111111111111111");
        Long id = serviceRepository.save(new Service(new Driver("vozko", "vojkovic", "192", location, vehicle, DriverState.AVAILABLE), ServiceType.TOWING, ServiceState.ASSIGNED));

        System.out.println("saved service(id " + id + ")");

        List<Service> services = serviceRepository.findAll();
        for (Service service : services) {
            System.out.println(service.getState());
            System.out.println(service.getAssignedDriver().getFirstName() + " " + service.getAssignedDriver().getLastName());
            System.out.println(service.getServiceType());
            System.out.println();
        }*/

        WorkshopRepository workshopRepository = new WorkshopRepository();
        Location location = new Location("address", "city", "country", "postalCode", new BigDecimal(45.8089772239981), new BigDecimal(15.716704654770382));

        Long id = workshopRepository.save(new Workshop("workshop_test", location));

        System.out.println("saved workshop record(id " + id + ")");

        List<Workshop> workshops = workshopRepository.findAll();
        for (Workshop workshop : workshops) {
            System.out.println(workshop.getName());
            System.out.println(workshop.getLocation().getAddress());
            System.out.println(workshop.getLocation().getCity());
            System.out.println(workshop.getLocation().getCountry());
            System.out.println(workshop.getLocation().getPostalCode());
            System.out.println();
        }
    }
}