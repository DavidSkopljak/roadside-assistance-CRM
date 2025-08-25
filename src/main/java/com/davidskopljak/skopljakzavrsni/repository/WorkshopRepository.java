package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.davidskopljak.skopljakzavrsni.entity.Workshop;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class WorkshopRepository extends AbstractRepository<Workshop> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);
    @Override
    public Workshop findById(Long id) throws SQLException {
        LOCK.lock();

        String vehicleQuery = "SELECT workshop.id, workshop.name, workshop.locationId, workshop.vehicle_model FROM workshop " +
                "INNER JOIN vehicle_model ON vehicle_model.id = workshop.vehicle_model_id" +
                "WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement vq = conn.prepareStatement(vehicleQuery);){
            vq.setLong(1, id);
            ResultSet rs = vq.executeQuery();

            if (rs.next()) {
                Long workshopId = rs.getLong("id");
                String name = rs.getString("name");
                Long locationId = rs.getLong("location_id");
                Location location = queryLocationById(locationId);
                VehicleModel vehicleModel = VehicleModel.valueOf(rs.getString("vehicle_model_id"));

                return new Workshop(workshopId, name, location, vehicleModel);
            }else{
                throw new EmptyResultSetException("workshop with id " + id + " not found");
            }
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public List<Workshop> findAll() throws SQLException {
        List<Workshop> workshops = new ArrayList<>();
        LOCK.lock();

        String vehicleQuery = "SELECT workshop.id, workshop.name, workshop.location_id, workshop.vehicle_model_id FROM workshop " +
                "INNER JOIN vehicle_model ON vehicle_model.id = workshop.vehicle_model_id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement vq = conn.prepareStatement(vehicleQuery);
             ResultSet rs = vq.executeQuery()){

            while(rs.next()) {
                Long workshopId = rs.getLong("id");
                String name = rs.getString("name");
                Long locationId = rs.getLong("location_id");
                Location location = queryLocationById(locationId);
                VehicleModel vehicleModel = RepositoryHelper.queryVehicleModelById(rs.getLong("vehicle_model_id"), conn);

                workshops.add(new Workshop(workshopId, name, location, vehicleModel));
            }
            return workshops;
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Workshop entity) throws SQLException {
        LOCK.lock();

        String sql = "INSERT INTO workshop (name, location_id, vehicle_model_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);){

            LocationRepository locationRepository = new LocationRepository();
            Long locationId = locationRepository.save(entity.getLocation());
            Long vehicleModelId = RepositoryHelper.queryVehicleModelByModel(entity.getPermittedVehicleModel(), conn);

            ps.setString(1, entity.getName());
            ps.setLong(2, locationId);
            ps.setLong(3, vehicleModelId);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No id retrieved for created workshop, possible issue with database");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Workshop> saveAll(List<Workshop> entities) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO workshop (name, location_id, vehicle_model_id) VALUES (?, ?, ?) RETURNING id";
        List<Workshop> saved = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            LocationRepository locationRepository = new LocationRepository();

            for (Workshop entity : entities) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    Long locationId = locationRepository.save(entity.getLocation());
                    Long modelId = RepositoryHelper.queryVehicleModelByModel(entity.getPermittedVehicleModel(), conn);

                    ps.setString(1, entity.getName());
                    ps.setLong(2, locationId);
                    ps.setLong(3, modelId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            saved.add(new Workshop(rs.getLong("id"), entity.getName(), entity.getLocation(), entity.getPermittedVehicleModel()));
                        } else {
                            throw new EmptyResultSetException("No id retrieved for workshop.");
                        }
                    }
                }
            }
        } finally {
            LOCK.unlock();
        }

        return saved;
    }

    @Override
    public void update(Workshop entity) throws SQLException {
        LOCK.lock();
        String sql = "UPDATE workshop SET name = ?, location_id = ?, vehicle_model_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocationRepository locationRepository = new LocationRepository();
            locationRepository.update(entity.getLocation());

            Long modelId = RepositoryHelper.queryVehicleModelByModel(entity.getPermittedVehicleModel(), conn);

            ps.setString(1, entity.getName());
            ps.setLong(2, entity.getLocation().getId());
            ps.setLong(3, modelId);
            ps.setLong(4, entity.getId());

            ps.executeUpdate();
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {

    }

    Location queryLocationById(Long locationId) throws SQLException {
        LocationRepository locationRepository = new LocationRepository();
        return locationRepository.findById(locationId);
    }
}
