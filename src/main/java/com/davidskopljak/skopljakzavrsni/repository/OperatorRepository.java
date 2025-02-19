package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Operator;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class OperatorRepository extends AbstractRepository<Operator> {
    private static ReentrantLock LOCK = new ReentrantLock();
    @Override
    public Operator findById(Long id) throws SQLException {
        LOCK.lock();
        String sql = "SELECT operator.id, operator.first_name, operator.last_name, operator.contact_number FROM operator WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return new Operator(id, rs.getString("first_name"), rs.getString("last_name"));
                }else{
                    throw new EmptyResultSetException("Operator with id " + id + " not found");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public List<Operator> findAll() throws SQLException {
        LOCK.lock();
        List<Operator> operators = new ArrayList<>();
        String sql = "SELECT operator.id, operator.first_name, operator.last_name FROM operator WHERE 1 = 1";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()) {
                operators.add(new Operator(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")));
            }
            return operators;

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public Long save(Operator entity) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO operator (first_name, last_name) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

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
