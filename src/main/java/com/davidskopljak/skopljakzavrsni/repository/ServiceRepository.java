package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.entity.Service;
import com.davidskopljak.skopljakzavrsni.enums.ServiceState;
import com.davidskopljak.skopljakzavrsni.enums.ServiceType;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceRepository extends AbstractRepository<Service> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    @Override
    public Service findById(Long id) throws SQLException {
        LOCK.lock();

        String sql = "SELECT service.id, service.assigned_driver_id, service.service_type_id, service.service_state_id, service.service_notes FROM service WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    Long serviceId = rs.getLong("id");
                    Long assignedDriverId = rs.getLong("assigned_driver_id");
                    Long serviceTypeId = rs.getLong("service_type_id");
                    Long serviceStateId = rs.getLong("service_state_id");
                    String serviceNotes = rs.getString("service_notes");

                    Driver assignedDriver = queryAssignedDriverById(assignedDriverId);
                    ServiceType serviceType = queryServiceTypeById(serviceTypeId, conn);
                    ServiceState serviceState = queryServiceStateById(serviceStateId, conn);

                    Service service = new Service(serviceId, assignedDriver, serviceType, serviceState);
                    if(!"[]".equals(serviceNotes)) {
                        service.setNotes(serviceNotes);
                    }

                    return service;
                }else{
                    throw new EmptyResultSetException("service with id " + id + " not found");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        LOCK.lock();

        String sql = "SELECT service.id, service.assigned_driver_id, service.service_type_id, service.service_state_id, service.service_notes FROM service WHERE 1 = 1";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    Long serviceId = rs.getLong("id");
                    Long assignedDriverId = rs.getLong("assigned_driver_id");
                    Long serviceTypeId = rs.getLong("service_type_id");
                    Long serviceStateId = rs.getLong("service_state_id");
                    String serviceNotes = rs.getString("service_notes");

                    Driver assignedDriver = queryAssignedDriverById(assignedDriverId);
                    ServiceType serviceType = queryServiceTypeById(serviceTypeId, conn);
                    ServiceState serviceState = queryServiceStateById(serviceStateId, conn);

                    Service service = new Service(serviceId, assignedDriver, serviceType, serviceState);
                    if(!"[]".equals(serviceNotes)) {
                        service.setNotes(serviceNotes);
                    }

                    services.add(service);
                }
                return services;
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Service entity) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO service (assigned_driver_id, service_type_id, service_state_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            DriverRepository driverRepository = new DriverRepository();
            Long driverId = driverRepository.save(entity.getAssignedDriver());

            Long serviceTypeId = queryServiceTypeByType(entity.getServiceType(), conn);

            Long serviceStateId = queryServiceStateByState(entity.getState(), conn);


            ps.setLong(1, driverId);
            ps.setLong(2, serviceTypeId);
            ps.setLong(3, serviceStateId);
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

    private Driver queryAssignedDriverById(Long id) throws SQLException {
        DriverRepository driverRepository = new DriverRepository();
        return(driverRepository.findById(id));
    }

    private ServiceType queryServiceTypeById(Long id, Connection conn) throws SQLException {
        String sql = "SELECT service_type.type FROM service_type WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return ServiceType.valueOf(rs.getString("type"));
                }else{
                    throw new EmptyResultSetException("No service type retrieved, possible issue with database");
                }
            }
        }
    }

    private Long queryServiceTypeByType(ServiceType serviceType, Connection conn) throws SQLException {
        String sql = "SELECT service_type.id FROM service_type WHERE type = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, serviceType.toString());
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No service type id retrieved, possible issue with database");
                }
            }
        }
    }

    private ServiceState queryServiceStateById(Long id, Connection conn) throws SQLException {
        String sql = "SELECT service_state.state FROM service_state WHERE id = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return ServiceState.valueOf(rs.getString("state"));
                }else{
                    throw new EmptyResultSetException("No service state retrieved, possible issue with database");
                }
            }
        }
    }

    private Long queryServiceStateByState(ServiceState serviceState, Connection conn) throws SQLException {
        String sql = "SELECT service_state.id FROM service_state WHERE state = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, serviceState.toString());
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No service state id retrieved, possible issue with database");
                }
            }
        }
    }
}
