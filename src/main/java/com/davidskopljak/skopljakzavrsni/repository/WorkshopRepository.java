package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.davidskopljak.skopljakzavrsni.entity.Vehicle;
import com.davidskopljak.skopljakzavrsni.entity.Workshop;
import com.davidskopljak.skopljakzavrsni.enums.VehicleModel;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class WorkshopRepository extends AbstractRepository<Workshop> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);
    @Override
    public Workshop findById(Long id) throws SQLException {
        LOCK.lock();

        String vehicleQuery = "SELECT workshop.id, workshop.name, workshop.locationId FROM workshop WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement vq = conn.prepareStatement(vehicleQuery);){
            vq.setLong(1, id);
            ResultSet rs = vq.executeQuery();

            if (rs.next()) {
                Long workshopId = rs.getLong("id");
                String name = rs.getString("name");
                Long locationId = rs.getLong("location_id");

                Location location = queryLocationById(locationId);

                return new Workshop(workshopId, name, location);
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

        String vehicleQuery = "SELECT workshop.id, workshop.name, workshop.location_id FROM workshop WHERE 1 = 1";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement vq = conn.prepareStatement(vehicleQuery);
             ResultSet rs = vq.executeQuery()){

            while(rs.next()) {
                Long workshopId = rs.getLong("id");
                String name = rs.getString("name");
                Long locationId = rs.getLong("location_id");

                Location location = queryLocationById(locationId);

                workshops.add(new Workshop(workshopId, name, location));
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

        String sql = "INSERT INTO workshop (name, location_id) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            LocationRepository locationRepository = new LocationRepository();
            Long locationId = locationRepository.save(entity.getLocation());

            ps.setString(1, entity.getName());
            ps.setLong(2, locationId);

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

    Location queryLocationById(Long locationId) throws SQLException {
        LocationRepository locationRepository = new LocationRepository();
        return locationRepository.findById(locationId);
    }
}
