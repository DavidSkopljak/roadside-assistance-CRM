package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.*;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class CaseRepository extends AbstractRepository<Case> {
    private static ReentrantLock LOCK = new ReentrantLock();
    @Override
    public Case findById(Long id) throws SQLException {
        LOCK.lock();
        String sql = "SELECT cases.id, cases.location_id, cases.first_operator_id, cases.last_edited_operator_id, cases.client_vehicle_id, cases.vehicle_first_registration_date, cases.damage_description, cases.case_state_id, cases.damage_type_id, cases.vehicle_damage_cause_id, cases.created_date_time, cases.active_service_id, cases.client_id FROM cases WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCase(rs, conn);
            }else{
                throw new EmptyResultSetException("Case with id " + id + " not found");
            }
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public List<Case> findAll() throws SQLException {
        List<Case> cases = new ArrayList<>();
        LOCK.lock();
        String sql = "SELECT cases.id, cases.location_id, cases.first_operator_id, cases.last_edited_operator_id, cases.client_vehicle_id, cases.damage_description, cases.case_state_id, cases.damage_type_id, cases.vehicle_damage_cause_id, cases.created_date_time, cases.active_service_id, cases.client_id, cases.vehicle_first_registration_date FROM cases WHERE 1 = 1";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()) {
                 cases.add(extractCase(rs, conn));
            }

            return cases;
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Case entity) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO cases (location_id, first_operator_id, last_edited_operator_id, client_vehicle_id, damage_description, case_state_id, damage_type_id, vehicle_damage_cause_id, created_date_time, active_service_id, client_id, vehicle_first_registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            LocationRepository locationRepository = new LocationRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();
            ServiceRepository serviceRepository = new ServiceRepository();
            ClientRepository clientRepository = new ClientRepository();

            Long locationId = locationRepository.save(entity.getLocation());
            ps.setLong(1, locationId);

            Long firstOperatorid = entity.getFirstOperator().getId();
            ps.setLong(2, firstOperatorid);

            Long lasteditedOperatorid = firstOperatorid;
            ps.setLong(3, lasteditedOperatorid);

            Long clientVehicleId = vehicleRepository.save(entity.getClientVehicle());
            ps.setLong(4, clientVehicleId);

            ps.setString(5, entity.getDamageDescription());

            Long caseStateId = RepositoryHelper.queryCaseStateByState(entity.getState(), conn);
            ps.setLong(6, caseStateId);

            Long damageTypeId = RepositoryHelper.queryVehicleDamageTypeByType(entity.getDamageType(), conn);
            ps.setLong(7, damageTypeId);

            Long damageCauseId = RepositoryHelper.queryVehicleDamageCauseByCause(entity.getDamageCause(), conn);
            ps.setLong(8, damageCauseId);

            ps.setTimestamp(9, Timestamp.valueOf(entity.getCreatedDateTime()));

            if(entity.getActiveService().isPresent()){
                Long activeServiceId = serviceRepository.save(entity.getActiveService().get());
                ps.setLong(10, activeServiceId);
            }else{
                ps.setNull(10, java.sql.Types.NULL);
            }

            Long clientId = clientRepository.save(entity.getClient());
            ps.setLong(11, clientId);

            ps.setTimestamp(12, Timestamp.valueOf(entity.getClientVehicleFirstRegistrationDate().atStartOfDay()));

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

    @Override
    public void update(Case entity) throws SQLException {
        LOCK.lock();
        String sql = "UPDATE cases SET location_id = ?, first_operator_id = ?, last_edited_operator_id = ?, client_vehicle_id = ?, damage_description = ?, case_state_id = ?, damage_type_id = ?, vehicle_damage_cause_id = ?, created_date_time = ?, active_service_id = ?, client_id = ?, vehicle_first_registration_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            LocationRepository locationRepository = new LocationRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();
            ServiceRepository serviceRepository = new ServiceRepository();
            ClientRepository clientRepository = new ClientRepository();

            locationRepository.update(entity.getLocation());
            ps.setLong(1, entity.getLocation().getId());

            Long firstOperatorid = entity.getFirstOperator().getId();
            ps.setLong(2, firstOperatorid);

            Long lasteditedOperatorid = entity.getLastEditedOperator().getId();
            ps.setLong(3, lasteditedOperatorid);

            vehicleRepository.update(entity.getClientVehicle());
            ps.setLong(4, entity.getClientVehicle().getId());

            ps.setString(5, entity.getDamageDescription());

            Long caseStateId = RepositoryHelper.queryCaseStateByState(entity.getState(), conn);
            ps.setLong(6, caseStateId);

            Long damageTypeId = RepositoryHelper.queryVehicleDamageTypeByType(entity.getDamageType(), conn);
            ps.setLong(7, damageTypeId);

            Long damageCauseId = RepositoryHelper.queryVehicleDamageCauseByCause(entity.getDamageCause(), conn);
            ps.setLong(8, damageCauseId);

            ps.setTimestamp(9, Timestamp.valueOf(entity.getCreatedDateTime()));

            if(entity.getActiveService().isPresent()){
                serviceRepository.update(entity.getActiveService().get());
                ps.setLong(10, entity.getActiveService().get().getId());
            }else{
                ps.setNull(10, java.sql.Types.NULL);
            }

            clientRepository.update(entity.getClient());
            ps.setLong(11, entity.getClient().getId());

            ps.setTimestamp(12, Timestamp.valueOf(entity.getClientVehicleFirstRegistrationDate().atStartOfDay()));

            ps.setLong(13, entity.getId());

            ps.executeUpdate();
        }catch(RepositoryAccessException | SQLException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {

    }

    @Override
    public List<Case> saveAll(List<Case> cases) throws SQLException {
        LOCK.lock();

        String sql = "INSERT INTO cases (location_id, first_operator_id, last_edited_operator_id, client_vehicle_id, " +
                "damage_description, case_state_id, damage_type_id, vehicle_damage_cause_id, created_date_time, " +
                "active_service_id, client_id, vehicle_first_registration_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        List<Case> savedCases = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            LocationRepository locationRepository = new LocationRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();
            ServiceRepository serviceRepository = new ServiceRepository();
            ClientRepository clientRepository = new ClientRepository();

            for (Case entity : cases) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {

                    Long locationId = locationRepository.save(entity.getLocation());
                    ps.setLong(1, locationId);

                    Long firstOperatorId = entity.getFirstOperator().getId();
                    ps.setLong(2, firstOperatorId);
                    ps.setLong(3, firstOperatorId);

                    Long clientVehicleId = vehicleRepository.save(entity.getClientVehicle());
                    ps.setLong(4, clientVehicleId);

                    ps.setString(5, entity.getDamageDescription());

                    Long caseStateId = RepositoryHelper.queryCaseStateByState(entity.getState(), conn);
                    ps.setLong(6, caseStateId);

                    Long damageTypeId = RepositoryHelper.queryVehicleDamageTypeByType(entity.getDamageType(), conn);
                    ps.setLong(7, damageTypeId);

                    Long damageCauseId = RepositoryHelper.queryVehicleDamageCauseByCause(entity.getDamageCause(), conn);
                    ps.setLong(8, damageCauseId);

                    ps.setTimestamp(9, Timestamp.valueOf(entity.getCreatedDateTime()));

                    if (entity.getActiveService().isPresent()) {
                        Long activeServiceId = serviceRepository.save(entity.getActiveService().get());
                        ps.setLong(10, activeServiceId);
                    } else {
                        ps.setNull(10, java.sql.Types.NULL);
                    }

                    Long clientId = clientRepository.save(entity.getClient());
                    ps.setLong(11, clientId);

                    ps.setTimestamp(12, Timestamp.valueOf(entity.getClientVehicleFirstRegistrationDate().atStartOfDay()));

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            entity.setId(rs.getLong("id"));
                            savedCases.add(entity);
                        } else {
                            throw new EmptyResultSetException("No id retrieved for Case: " + entity);
                        }
                    }
                }
            }

            return savedCases;

        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    private Case extractCase(ResultSet rs, Connection conn) throws SQLException {
        Long caseId = rs.getLong("id");
        Long locationId = rs.getLong("location_id");
        Long firstOperatorId = rs.getLong("first_operator_id");
        Long lastEditedOperatorId = rs.getLong("last_edited_operator_id");
        Long clientVehicleId = rs.getLong("client_vehicle_id");
        LocalDate clientVeicleFirstRegDate = rs.getTimestamp("vehicle_first_registration_date")
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        String damageDescription = rs.getString("damage_description");
        Long caseStateId = rs.getLong("case_state_id");
        Long damageTypeId = rs.getLong("damage_type_id");
        Long damageCauseId = rs.getLong("vehicle_damage_cause_id");
        Timestamp createdDateTime = rs.getTimestamp("created_date_time");
        Long clientId = rs.getLong("client_id");
        Long activeServiceId = rs.getLong("active_service_id");
        if(rs.wasNull()){
            activeServiceId = null;
        }

        LocationRepository locationRepository = new LocationRepository();
        OperatorRepository operatorRepository = new OperatorRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();
        ClientRepository clientRepository = new ClientRepository();
        ServiceRepository serviceRepository = new ServiceRepository();
        NoteRepository noteRepository = new NoteRepository();

        ArrayList<Note> caseNotes = new ArrayList<> (noteRepository.findAllById(caseId));
        Location location = locationRepository.findById(locationId);
        Operator firstOperator = operatorRepository.findById(firstOperatorId);
        Operator lastEditedOperator = operatorRepository.findById(lastEditedOperatorId);
        Vehicle clientVehicle = vehicleRepository.findById(clientVehicleId);
        Client client = clientRepository.findById(clientId);
        CaseState caseState = RepositoryHelper.queryCaseStateById(caseStateId, conn);
        VehicleDamageType damageType = RepositoryHelper.queryVehicleDamageTypeById(damageTypeId, conn);
        VehicleDamageCause damageCause = RepositoryHelper.queryVehicleDamageCauseById(damageCauseId, conn);

        Optional<Service> activeService;
        if(activeServiceId != null){
            activeService = Optional.of(serviceRepository.findById(activeServiceId));
        }else{
            activeService = Optional.empty();
        }

        Case newCase = new Case();

        newCase.setClient(client)
                .setClientVehicle(clientVehicle)
                .setClientVehicleFirstRegistrationDate(clientVeicleFirstRegDate)
                .setDamageCause(damageCause)
                .setDamageType(damageType)
                .setFirstOperator(firstOperator)
                .setLastEditedOperator(lastEditedOperator)
                .setLocation(location)
                .setDamageDescription(damageDescription)
                .setCreatedDateTime(createdDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .setActiveService(activeService)
                .setId(caseId);

        newCase.updateState(caseState);
        newCase.addNotes(caseNotes);
        return newCase;
    }
}