package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Driver;
import com.davidskopljak.skopljakzavrsni.entity.Service;
import com.davidskopljak.skopljakzavrsni.entity.Workshop;
import com.davidskopljak.skopljakzavrsni.enums.ServiceState;
import com.davidskopljak.skopljakzavrsni.enums.ServiceType;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceRepository extends AbstractRepository<Service> {
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    public List<Service> findAllByCaseId(Long searchCaseId) throws SQLException {
        List<Service> services = new ArrayList<>();
        LOCK.lock();

        String sql = "SELECT service.id, service.assigned_driver_id, service.service_type_id,service.service_state_id, service.driver_notes, service.case_id, service.workshop_id FROM service WHERE case_id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, searchCaseId);
            ResultSet rs = ps.executeQuery();
            WorkshopRepository workshopRepository = new WorkshopRepository();

            while (rs.next()) {
                Long serviceId = rs.getLong("id");
                Long assignedDriverId = rs.getLong("assigned_driver_id");
                Long serviceTypeId = rs.getLong("service_type_id");
                Long serviceStateId = rs.getLong("service_state_id");
                Long caseId = rs.getLong("case_id");
                String driverNotes = rs.getString("driver_notes");
                Long workshopId = rs.getLong("workshop_id");

                Driver assignedDriver = queryAssignedDriverById(assignedDriverId);
                ServiceType serviceType = RepositoryHelper.queryServiceTypeById(serviceTypeId, conn);
                ServiceState serviceState = RepositoryHelper.queryServiceStateById(serviceStateId, conn);
                Workshop workshop = workshopRepository.findById(workshopId);

                Service service = new Service(serviceId, caseId, assignedDriver, serviceType, workshop, serviceState, driverNotes);

                services.add(service);
            }
            return services;
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Service findById(Long id) throws SQLException {
        LOCK.lock();
        String sql = """
            SELECT service.id, service.assigned_driver_id, service.service_type_id,
                   service.service_state_id, service.driver_notes, service.case_id, service.workshop_id
            FROM service WHERE id = ?
        """;

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long serviceId = rs.getLong("id");
                    Long assignedDriverId = rs.getLong("assigned_driver_id");
                    Long serviceTypeId = rs.getLong("service_type_id");
                    Long serviceStateId = rs.getLong("service_state_id");
                    Long caseId = rs.getLong("case_id");
                    String driverNotes = rs.getString("driver_notes");

                    WorkshopRepository workshopRepository = new WorkshopRepository();
                    Workshop workshop = workshopRepository.findById(rs.getLong("workshop_id"));

                    Driver assignedDriver = queryAssignedDriverById(assignedDriverId);
                    ServiceType serviceType = RepositoryHelper.queryServiceTypeById(serviceTypeId, conn);
                    ServiceState serviceState = RepositoryHelper.queryServiceStateById(serviceStateId, conn);

                    return new Service(serviceId, caseId, assignedDriver, serviceType, workshop, serviceState, driverNotes);
                } else {
                    throw new EmptyResultSetException("Service with id " + id + " not found");
                }
            }
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        LOCK.lock();

        String sql = """
        SELECT service.id, service.assigned_driver_id, service.service_type_id,
               service.service_state_id, service.driver_notes, service.case_id, service.workshop_id
        FROM service
    """;

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            WorkshopRepository workshopRepository = new WorkshopRepository();

            while (rs.next()) {
                Long serviceId = rs.getLong("id");
                Long assignedDriverId = rs.getLong("assigned_driver_id");
                Long serviceTypeId = rs.getLong("service_type_id");
                Long serviceStateId = rs.getLong("service_state_id");
                Long caseId = rs.getLong("case_id");
                String driverNotes = rs.getString("driver_notes");
                Long workshopId = rs.getLong("workshop_id");

                Driver assignedDriver = queryAssignedDriverById(assignedDriverId);
                ServiceType serviceType = RepositoryHelper.queryServiceTypeById(serviceTypeId, conn);
                ServiceState serviceState = RepositoryHelper.queryServiceStateById(serviceStateId, conn);
                Workshop workshop = workshopRepository.findById(workshopId);

                Service service = new Service(serviceId, caseId, assignedDriver, serviceType, workshop, serviceState, driverNotes);

                services.add(service);
            }
            return services;
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }


    @Override
    public Long save(Service entity) throws SQLException {
        LOCK.lock();
        String sql = """
            INSERT INTO service (assigned_driver_id, service_type_id, service_state_id, case_id, driver_notes, workshop_id)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING id
        """;

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            DriverRepository driverRepository = new DriverRepository();
            Long driverId = driverRepository.save(entity.getAssignedDriver());
            Long serviceTypeId = RepositoryHelper.queryServiceTypeByType(entity.getServiceType(), conn);
            Long serviceStateId = RepositoryHelper.queryServiceStateByState(entity.getState(), conn);

            ps.setLong(1, driverId);
            ps.setLong(2, serviceTypeId);
            ps.setLong(3, serviceStateId);
            ps.setLong(4, entity.getCaseId());
            ps.setString(5, entity.getDriverNotes());
            ps.setLong(6, entity.getWorkshop().getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                } else {
                    throw new EmptyResultSetException("No id retrieved for created service, possible issue with database");
                }
            }
        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Service> saveAll(List<Service> entities) throws SQLException {
        LOCK.lock();
        String sql = """
            INSERT INTO service (assigned_driver_id, service_type_id, service_state_id, case_id, driver_notes, workshop_id)
            VALUES (?, ?, ?, ?, ?, ?) RETURNING id
        """;
        List<Service> saved = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            for (Service entity : entities) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    DriverRepository driverRepository = new DriverRepository();
                    Long driverId = driverRepository.save(entity.getAssignedDriver());
                    Long serviceTypeId = RepositoryHelper.queryServiceTypeByType(entity.getServiceType(), conn);
                    Long serviceStateId = RepositoryHelper.queryServiceStateByState(entity.getState(), conn);

                    ps.setLong(1, driverId);
                    ps.setLong(2, serviceTypeId);
                    ps.setLong(3, serviceStateId);
                    ps.setLong(4, entity.getCaseId());
                    ps.setString(5, entity.getDriverNotes());
                    ps.setLong(6, entity.getWorkshop().getId());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            entity.setId(rs.getLong("id"));
                            saved.add(entity);
                        } else {
                            throw new EmptyResultSetException("No id retrieved for created service.");
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
    public void update(Service entity) throws SQLException {
        LOCK.lock();
        String sql = """
            UPDATE service
            SET assigned_driver_id = ?, service_type_id = ?, service_state_id = ?, driver_notes = ?, workshop_id = ?
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            Long serviceTypeId = RepositoryHelper.queryServiceTypeByType(entity.getServiceType(), conn);
            Long serviceStateId = RepositoryHelper.queryServiceStateByState(entity.getState(), conn);

            ps.setLong(1, entity.getAssignedDriver().getId());
            ps.setLong(2, serviceTypeId);
            ps.setLong(3, serviceStateId);
            ps.setString(4, entity.getDriverNotes());
            ps.setLong(5, entity.getWorkshop().getId());
            ps.setLong(6, entity.getId());

            ps.executeUpdate();
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        LOCK.lock();
        String sql = "DELETE FROM service WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } finally {
            LOCK.unlock();
        }
    }

    private Driver queryAssignedDriverById(Long id) throws SQLException {
        DriverRepository driverRepository = new DriverRepository();
        return driverRepository.findById(id);
    }
}
