package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.*;
import com.davidskopljak.skopljakzavrsni.enums.*;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.RepositoryHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.*;
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
        String sql = "SELECT case_table.id, case_table.location_id, case_table.first_operator_id, case_table.last_edited_operator_id, case_table.client_vehicle_id, case_table.damage_description, case_table.case_state_id, case_table.damage_type_id, case_table.vehicle_damage_cause_id, case_table.created_date_time, case_table.active_service_id, case_table.client_id, case_table.case_notes FROM case_table WHERE id = ?";

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
        String sql = "SELECT case_table.id, case_table.location_id, case_table.first_operator_id, case_table.last_edited_operator_id, case_table.client_vehicle_id, case_table.damage_description, case_table.case_state_id, case_table.damage_type_id, case_table.vehicle_damage_cause_id, case_table.created_date_time, case_table.active_service_id, case_table.client_id, case_table.case_notes FROM case_table WHERE 1 = 1";

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
        String sql = "INSERT INTO case_table (location_id, first_operator_id, last_edited_operator_id, client_vehicle_id, damage_description, case_state_id, damage_type_id, vehicle_damage_cause_id, created_date_time, active_service_id, client_id, case_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            LocationRepository locationRepository = new LocationRepository();
            OperatorRepository operatorRepository = new OperatorRepository();
            VehicleRepository vehicleRepository = new VehicleRepository();
            ServiceRepository serviceRepository = new ServiceRepository();
            ClientRepository clientRepository = new ClientRepository();

            Long locationId = locationRepository.save(entity.getLocation());
            ps.setLong(1, locationId);

            Long firstOperatorid = operatorRepository.save(entity.getFirstOperator());
            ps.setLong(2, firstOperatorid);

            Long lasteditedOperatorid = operatorRepository.save(entity.getLastEditedOperator());
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
            }

            Long clientId = clientRepository.save(entity.getClient());
            ps.setLong(11, clientId);

            ps.setObject(12, "[]", Types.OTHER);

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

    /*public void addNote(Long caseId, Note note) throws SQLException {
        LOCK.lock();
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            String sql = "UPDATE case_table SET case_notes = case_notes || ?::jsonb WHERE id = ?;";

            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                // Create and configure the ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule

                // Serialize the Note object to JSON
                String noteJson = "[" + mapper.writeValueAsString(note) + "]";
                System.out.println("case repository - json note: " + noteJson);

                // Set the JSON value as a parameter
                preparedStatement.setString(1, noteJson);
                preparedStatement.setLong(2, caseId); // Set the case ID parameter
                preparedStatement.executeUpdate(); // Execute the update
            }
        } catch (RepositoryAccessException | SQLException | JsonProcessingException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }*/

    private Location queryLocationById(Long locationId) throws SQLException {
        LocationRepository locationRepository = new LocationRepository();
        return locationRepository.findById(locationId);
    }

    private Operator queryOperatorById(Long operatorId) throws SQLException {
        OperatorRepository operatorRepository = new OperatorRepository();
        return operatorRepository.findById(operatorId);
    }

    private Vehicle queryVehicleById(Long vehicleId) throws SQLException {
        VehicleRepository vehicleRepository = new VehicleRepository();
        return vehicleRepository.findById(vehicleId);
    }

    private Client queryClientById(Long clientId) throws SQLException {
        ClientRepository clientRepository = new ClientRepository();
        return clientRepository.findById(clientId);
    }

    private Service queryServiceById(Long serviceId) throws SQLException {
        ServiceRepository serviceRepository = new ServiceRepository();
        return serviceRepository.findById(serviceId);
    }




    private Case extractCase(ResultSet rs, Connection conn) throws SQLException {
        Long caseId = rs.getLong("id");
        Long locationId = rs.getLong("location_id");
        Long firstOperatorId = rs.getLong("first_operator_id");
        Long lastEditedOperatorId = rs.getLong("last_edited_operator_id");
        Long clientVehicleId = rs.getLong("client_vehicle_id");
        String damageDescription = rs.getString("damage_description");
        Long caseStateId = rs.getLong("case_state_id");
        Long damageTypeId = rs.getLong("damage_type_id");
        Long damageCauseId = rs.getLong("vehicle_damage_cause_id");
        Timestamp createdDateTime = rs.getTimestamp("created_date_time");
        Long clientId = rs.getLong("client_id");
        String caseNotes = rs.getString("case_notes");
        System.out.println("Case notes in CaseRepository.extractCase() - Case id: " + caseId + " Case notes: " + caseNotes);
        if(rs.wasNull()){
            caseNotes = "";
        }
        Long activeServiceId = rs.getLong("active_service_id");
        if(rs.wasNull()){
            activeServiceId = null;
        }

        Location location = queryLocationById(locationId);
        Operator firstOperator = queryOperatorById(firstOperatorId);
        Operator lastEditedOperator = queryOperatorById(lastEditedOperatorId);
        Vehicle clientVehicle = queryVehicleById(clientVehicleId);
        Client client = queryClientById(clientId);
        CaseState caseState = RepositoryHelper.queryCaseStateById(caseStateId, conn);
        VehicleDamageType damageType = RepositoryHelper.queryVehicleDamageTypeById(damageTypeId, conn);
        VehicleDamageCause damageCause = RepositoryHelper.queryVehicleDamageCauseById(damageCauseId, conn);

        Optional<Service> activeService;
        if(activeServiceId != null){
            activeService = Optional.of(queryServiceById(activeServiceId));
        }else{
            activeService = Optional.empty();
        }

        Case newCase = new Case();

        newCase.setClient(client)
                .setClientVehicle(clientVehicle)
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
        System.out.println("Right before case.setNotes()");
        newCase.setNotes(caseNotes);
        return newCase;
    }
}