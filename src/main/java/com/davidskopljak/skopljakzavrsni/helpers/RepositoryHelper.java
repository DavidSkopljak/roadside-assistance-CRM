package com.davidskopljak.skopljakzavrsni.helpers;

import com.davidskopljak.skopljakzavrsni.enums.*;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryHelper {

    private RepositoryHelper() {}

    public static CaseState queryCaseStateById(Long caseStateId, Connection conn) throws SQLException {
        String vehicleModelQuery = "SELECT case_state.state FROM case_state WHERE id = ?";
        try(PreparedStatement vmq = conn.prepareStatement(vehicleModelQuery)){
            vmq.setLong(1, caseStateId);
            try(ResultSet rs = vmq.executeQuery();){
                if(rs.next()) {
                    return CaseState.valueOf(rs.getString("state"));
                }else{
                    throw new EmptyResultSetException("No case state retrieved, possible issue with database");
                }
            }
        }
    }

    public static VehicleDamageType queryVehicleDamageTypeById(Long damageTypeId, Connection conn) throws SQLException {
        String vehicleModelQuery = "SELECT vehicle_damage_type.damage_type FROM vehicle_damage_type WHERE id = ?";
        try(PreparedStatement vmq = conn.prepareStatement(vehicleModelQuery)){
            vmq.setLong(1, damageTypeId);
            try(ResultSet rs = vmq.executeQuery();){
                if(rs.next()) {
                    return VehicleDamageType.valueOf(rs.getString("damage_type"));
                }else{
                    throw new EmptyResultSetException("No vehicle damage type retrieved, possible issue with database");
                }
            }
        }
    }

    public static VehicleDamageCause queryVehicleDamageCauseById(Long damageCauseId, Connection conn) throws SQLException {
        String vehicleModelQuery = "SELECT vehicle_damage_cause.damage_cause FROM vehicle_damage_cause WHERE id = ?";
        try(PreparedStatement vmq = conn.prepareStatement(vehicleModelQuery)){
            vmq.setLong(1, damageCauseId);
            try(ResultSet rs = vmq.executeQuery();){
                if(rs.next()) {
                    return VehicleDamageCause.valueOf(rs.getString("damage_cause"));
                }else{
                    throw new EmptyResultSetException("No vehicle damage cause retrieved, possible issue with database");
                }
            }
        }
    }

    public static Long queryCaseStateByState(CaseState caseState, Connection conn) throws SQLException {
        String sql = "SELECT case_state.id FROM case_state WHERE state = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, caseState.toString());
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No case state id retrieved, possible issue with database");
                }
            }
        }
    }
    public static Long queryVehicleDamageTypeByType(VehicleDamageType vehicleDamageType, Connection conn) throws SQLException {
        String sql = "SELECT vehicle_damage_type.id FROM vehicle_damage_type WHERE damage_type = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, vehicleDamageType.toString());
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No vehicle damage type id retrieved, possible issue with database");
                }
            }
        }
    }

    public static Long queryVehicleDamageCauseByCause(VehicleDamageCause vehicleDamageCause, Connection conn) throws SQLException {
        String sql = "SELECT vehicle_damage_cause.id FROM vehicle_damage_cause WHERE damage_cause = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, vehicleDamageCause.toString());
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No vehicle damage cause id retrieved, possible issue with database");
                }
            }
        }
    }

    public static DriverState queryDriverStateById(Long driverStateId, Connection conn) throws SQLException {
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

    public static Long queryDriverStateByState(DriverState driverState, Connection conn) throws SQLException {
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

    public static VehicleModel queryVehicleModelById(Long vehicleModelId, Connection conn) throws SQLException {
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

    public static Long queryVehicleModelByModel(VehicleModel model, Connection conn) throws SQLException {
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

    public static ArrayList<VehicleModel> queryAllVehicleModels(Connection conn) throws SQLException {
        String vehicleModelQuery = "SELECT vehicle_model.model FROM vehicle_model";
        ArrayList<VehicleModel> models = new ArrayList<>();
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(vehicleModelQuery)){
                while (rs.next()) {
                    VehicleModel model = VehicleModel.valueOf(rs.getString("model"));
                    models.add(model);
                }

                if (models.isEmpty()) {
                    throw new EmptyResultSetException("No vehicle models retrieved from the database.");
                }

                return models;
            }
        }
    }

    public static List<VehicleDamageType> queryAllVehicleDamageTypes(Connection conn) throws SQLException {
        String vehicleDamageTypeQuery = "SELECT vehicle_damage_type.damage_type FROM vehicle_damage_type";
        ArrayList<VehicleDamageType> types = new ArrayList<>();
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(vehicleDamageTypeQuery)){
                while (rs.next()) {
                    VehicleDamageType type = VehicleDamageType.valueOf(rs.getString("damage_type"));
                    types.add(type);
                }

                if (types.isEmpty()) {
                    throw new EmptyResultSetException("No vehicle models retrieved from the database.");
                }

                return types;
            }
        }
    }

    public static List<VehicleDamageCause> queryAllVehicleDamageCauses(Connection conn) throws SQLException {
        String vehicleDamageCauseQuery = "SELECT vehicle_damage_cause.damage_cause FROM vehicle_damage_cause";
        ArrayList<VehicleDamageCause> causes = new ArrayList<>();
        try(Statement stmt = conn.createStatement()){
            try(ResultSet rs = stmt.executeQuery(vehicleDamageCauseQuery)){
                while (rs.next()) {
                    VehicleDamageCause cause = VehicleDamageCause.valueOf(rs.getString("damage_cause"));
                    causes.add(cause);
                }

                if (causes.isEmpty()) {
                    throw new EmptyResultSetException("No vehicle models retrieved from the database.");
                }

                return causes;
            }
        }
    }

    public static ServiceType queryServiceTypeById(Long id, Connection conn) throws SQLException {
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

    public static Long queryServiceTypeByType(ServiceType serviceType, Connection conn) throws SQLException {
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

    public static ServiceState queryServiceStateById(Long id, Connection conn) throws SQLException {
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

    public static Long queryServiceStateByState(ServiceState serviceState, Connection conn) throws SQLException {
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
