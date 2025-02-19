package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Location;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LocationRepository extends AbstractRepository<Location> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    @Override
    public Location findById(Long id) throws SQLException {
        LOCK.lock();

        String sql = "SELECT location.id, location.address, location.city, location.country, location.postal_code, location.coordinates_x, location.coordinates_y FROM location WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Long locationId = rs.getLong("id");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postalCode = rs.getString("postal_code");
                BigDecimal coordinatesX = rs.getBigDecimal("coordinates_x");
                BigDecimal coordinatesY = rs.getBigDecimal("coordinates_y");

                return new Location(locationId, address, city, country, postalCode, coordinatesX, coordinatesY);

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
    public List<Location> findAll() throws SQLException {
        LOCK.lock();
        List<Location> locations = new ArrayList<>();

        String sql = "SELECT location.id, location.address, location.city, location.country, location.postal_code, location.coordinates_x, location.coordinates_y FROM location WHERE 1 = 1";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             final PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                Long locationId = rs.getLong("id");
                String address = rs.getString("address");
                String city = rs.getString("city");
                String country = rs.getString("country");
                String postalCode = rs.getString("postal_code");
                BigDecimal coordinatesX = rs.getBigDecimal("coordinates_x");
                BigDecimal coordinatesY = rs.getBigDecimal("coordinates_y");

                locations.add(new Location(locationId, address, city, country, postalCode, coordinatesX, coordinatesY));
            }
            return locations;
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Location entity) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO location (address, city, country, postal_code, coordinates_x, coordinates_y) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getAddress());
            ps.setString(2, entity.getCity());
            ps.setString(3, entity.getCountry());
            ps.setString(4, entity.getPostalCode());
            ps.setBigDecimal(5, entity.getCoordinatesX());
            ps.setBigDecimal(6, entity.getCoordinatesY());

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No id retrieved for created operator, possible issue with database");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }
}
