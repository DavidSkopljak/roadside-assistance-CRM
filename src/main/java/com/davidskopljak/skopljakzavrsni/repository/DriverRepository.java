package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.davidskopljak.skopljakzavrsni.entity.Vehicle;
import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DriverRepository extends AbstractRepository<Driver> {
    private static ReentrantLock LOCK = new ReentrantLock();
    @Override
    public Driver findById(Long id) throws SQLException {
        LOCK.lock();
        String sql = "SELECT driver.id, driver.first_name, driver.last_name, driver.contact_number, driver.vehicle_id, driver.current_location_id, driver.driver_state_id FROM driver WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long driverId = rs.getLong(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String contactNumber = rs.getString(4);
                Long vehicleId = rs.getLong(5);
                Long currentLocationId = rs.getLong(6);
                if(rs.wasNull()){currentLocationId = null;}
                Long driverStateId = rs.getLong(7);

                DriverState driverState = queryDriverStateById(driverStateId, conn);
                Location currentLocation = queryLocationById(currentLocationId);
                Vehicle vehicle = queryVehicleById(vehicleId);

                return new Driver(driverId, firstName, lastName, contactNumber, currentLocation, vehicle, driverState);
            }else{
                throw new EmptyResultSetException("Driver with id " + id + " not found");
            }
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Driver> findAll() throws SQLException {
        LOCK.lock();
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT driver.id, driver.first_name, driver.last_name, driver.contact_number, driver.vehicle_id, driver.current_location_id, driver.driver_state_id FROM driver WHERE 1 = 1";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()) {
                Long driverId = rs.getLong(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String contactNumber = rs.getString(4);
                Long vehicleId = rs.getLong(5);
                Long currentLocationId = rs.getLong(6);
                if(rs.wasNull()){currentLocationId = null;}
                Long driverStateId = rs.getLong(7);

                DriverState driverState = queryDriverStateById(driverStateId, conn);
                Location currentLocation = queryLocationById(currentLocationId);
                Vehicle vehicle = queryVehicleById(vehicleId);

                drivers.add(new Driver(driverId, firstName, lastName, contactNumber, currentLocation, vehicle, driverState));
            }
            return drivers;

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Driver entity) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO driver (first_name, last_name, contact_number, vehicle_id, current_location_id, driver_state_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            
            VehicleRepository vehicleRepository = new VehicleRepository();
            Long vehicleId = vehicleRepository.save(entity.getVehicle());
            
            LocationRepository locationRepository = new LocationRepository();
            Long locationId = locationRepository.save(entity.getCurrentLocation());
            System.out.println("LocationId: " + locationId);
            
            Long driverStateId = queryDriverStateByState(entity.getState(), conn);


            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getContactNumber());
            ps.setLong(4, vehicleId);
            ps.setLong(5, locationId);
            ps.setLong(6, driverStateId);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No id retrieved for created driver, possible issue with database");
                }
            }

        }catch(RepositoryAccessException | SQLException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    DriverState queryDriverStateById(Long driverStateId, Connection conn) throws SQLException {
        String sql = "SELECT driver_state.state FROM driver_state WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, driverStateId);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return DriverState.valueOf(rs.getString("state"));
                }else{
                    throw new EmptyResultSetException("No driver state retrieved, possible issue with database");
                }
            }
        }
    }

    Long queryDriverStateByState(DriverState driverState, Connection conn) throws SQLException {
        String sql = "SELECT driver_state.id FROM driver_state WHERE state = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, driverState.toString());
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No driver state id retrieved, possible issue with database");
                }
            }
        }
    }

    Location queryLocationById(Long locationId) throws SQLException {
        LocationRepository locationRepository = new LocationRepository();
        return locationRepository.findById(locationId);
    }

    Vehicle queryVehicleById(Long vehicleId) throws SQLException {
        VehicleRepository vehicleRepository = new VehicleRepository();
        return vehicleRepository.findById(vehicleId);
    }

}
