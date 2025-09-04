package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.enums.DriverState;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class DriverRepository extends AbstractRepository<Driver> {
    private static ReentrantLock LOCK = new ReentrantLock();

    @Override
    public Driver findById(Long id) throws SQLException {
        LOCK.lock();
        String sql = "SELECT driver.id, driver.first_name, driver.last_name, driver.contact_number, driver.driver_state_id FROM driver WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long driverId = rs.getLong(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String contactNumber = rs.getString(4);
                Long driverStateId = rs.getLong(5);

                DriverState driverState = RepositoryHelper.queryDriverStateById(driverStateId, conn);

                return new Driver(driverId, firstName, lastName, contactNumber, driverState);
            } else {
                throw new EmptyResultSetException("Driver with id " + id + " not found");
            }
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Driver> findAll() {
        LOCK.lock();
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT driver.id, driver.first_name, driver.last_name, driver.contact_number, driver.driver_state_id FROM driver";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Long driverId = rs.getLong(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String contactNumber = rs.getString(4);
                Long driverStateId = rs.getLong(5);

                DriverState driverState = RepositoryHelper.queryDriverStateById(driverStateId, conn);

                drivers.add(new Driver(driverId, firstName, lastName, contactNumber, driverState));
            }
            return drivers;

        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Driver entity) {
        LOCK.lock();
        String sql = "INSERT INTO driver (first_name, last_name, contact_number, driver_state_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Long driverStateId = RepositoryHelper.queryDriverStateByState(entity.getState(), conn);

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getContactNumber());
            ps.setLong(4, driverStateId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                } else {
                    throw new EmptyResultSetException("No id retrieved for created driver, possible issue with database");
                }
            }

        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void update(Driver entity) {
        LOCK.lock();
        String sql = "UPDATE driver SET first_name = ?, last_name = ?, contact_number = ?, driver_state_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Long driverStateId = RepositoryHelper.queryDriverStateByState(entity.getState(), conn);

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getContactNumber());
            ps.setLong(4, driverStateId);
            ps.setLong(5, entity.getId());

            ps.executeUpdate();

        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void deleteById(Long id) {
        if(!CRMApplication.getActiveOperator().getUsername().equals("admin")){
            throw new RepositoryAccessException("Only admin can delete drivers");
        }

        LOCK.lock();

        String deleteDriverSql = "DELETE FROM driver WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteDriverSql);) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryAccessException("Something went wrong while trying to delete driver with id " + id + ". " + e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Driver> saveAll(List<Driver> entities) {
        LOCK.lock();
        List<Driver> savedDrivers = new ArrayList<>();
        String sql = "INSERT INTO driver (first_name, last_name, contact_number, driver_state_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Driver entity : entities) {
                Long driverStateId = RepositoryHelper.queryDriverStateByState(entity.getState(), conn);

                ps.setString(1, entity.getFirstName());
                ps.setString(2, entity.getLastName());
                ps.setString(3, entity.getContactNumber());
                ps.setLong(4, driverStateId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long generatedId = rs.getLong("id");
                        Driver savedDriver = new Driver(
                                generatedId,
                                entity.getFirstName(),
                                entity.getLastName(),
                                entity.getContactNumber(),
                                entity.getState()
                        );
                        savedDrivers.add(savedDriver);
                    } else {
                        throw new EmptyResultSetException("No ID returned for inserted driver.");
                    }
                }
            }

            return savedDrivers;

        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }
}
