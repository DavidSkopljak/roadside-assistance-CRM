package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Vehicle;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class VehicleRepository extends AbstractRepository<Vehicle> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    @Override
    public Vehicle findById(Long id) throws SQLException {
        LOCK.lock();

        String vehicleQuery = "SELECT vehicle.id, vehicle.license_plate, vehicle.vin, vehicle.first_registration_date, vehicle.vehicle_model_id FROM vehicle WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement vq = conn.prepareStatement(vehicleQuery);){
            vq.setLong(1, id);
            ResultSet rs = vq.executeQuery();

            if (rs.next()) {
                Long vehicleId = rs.getLong("id");
                String licensePlate = rs.getString("license_plate");
                Long vehicleModelId = rs.getLong("vehicle_model_id");
                if(rs.wasNull()){vehicleModelId = null;}
                String vin = rs.getString("vin");
                LocalDate firstRegDate = rs.getObject("first_registration_date", LocalDate.class);

                VehicleModel vehicleModel = (vehicleModelId!=null) ? queryVehicleModelById(vehicleModelId, conn) : null;

                return new Vehicle(vehicleId, licensePlate, vehicleModel, firstRegDate, vin);
            }else{
                throw new EmptyResultSetException("Vehicle with id " + id + " not found");
            }
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public synchronized List<Vehicle> findAll() throws SQLException {
        LOCK.lock();
        List<Vehicle> vehicles = new ArrayList<>();
        String vehicleQuery = "SELECT vehicle.id, vehicle.license_plate, vehicle.vin, vehicle.first_registration_date, vehicle.vehicle_model_id FROM vehicle WHERE 1 = 1";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement vq = conn.prepareStatement(vehicleQuery);){
            ResultSet rs = vq.executeQuery();

            while (rs.next()) {
                Long vehicleId = rs.getLong("id");
                String licensePlate = rs.getString("license_plate");
                Long vehicleModelId = rs.getLong("vehicle_model_id");
                if(rs.wasNull()){vehicleModelId = null;}
                String vin = rs.getString("vin");
                LocalDateTime ldt = rs.getObject("first_registration_date", LocalDateTime.class);
                LocalDate firstRegDate = (ldt != null) ? ldt.toLocalDate() : null;

                VehicleModel vehicleModel = (vehicleModelId!=null) ? queryVehicleModelById(vehicleModelId, conn) : null;

                vehicles.add(new Vehicle(vehicleId, licensePlate, vehicleModel, firstRegDate, vin));
            }
            return vehicles;
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public synchronized Long save(Vehicle entity) throws SQLException {
        LOCK.lock();

        String vehicleQuery = "INSERT INTO vehicle (license_plate, vin, vehicle_model_id, first_registration_date) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(vehicleQuery)){
            Long vehicleModelId = queryVehicleModelByModel(entity.getModel(), conn);
                ps.setString(1, entity.getLicensePlate());
                ps.setString(2, entity.getVin());
                ps.setLong(3, vehicleModelId);
                ps.setObject(4, entity.getFirstRegistrationDate());

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No id retrieved for created vehicle, possible issue with database");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    VehicleModel queryVehicleModelById(Long vehicleModelId, Connection conn) throws SQLException {
        String vehicleModelQuery = "SELECT vehicle_model.model FROM vehicle_model WHERE id = ?";
        try(PreparedStatement vmq = conn.prepareStatement(vehicleModelQuery)){
            vmq.setLong(1, vehicleModelId);
            try(ResultSet rs = vmq.executeQuery();){
                if(rs.next()) {
                    return VehicleModel.valueOf(rs.getString("model"));
                }else{
                    throw new EmptyResultSetException("No vehicle model retrieved, possible issue with database");
                }
            }
        }
    }

    Long queryVehicleModelByModel(VehicleModel model, Connection conn) throws SQLException {
        String vehicleModelQuery = "SELECT vehicle_model.id FROM vehicle_model WHERE model = ?";
        try(PreparedStatement vmq = conn.prepareStatement(vehicleModelQuery)){
            vmq.setString(1, model.toString());
            try(ResultSet rs = vmq.executeQuery()){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No vehicle model id retrieved, possible issue with database");
                }
            }
        }
    }
}
