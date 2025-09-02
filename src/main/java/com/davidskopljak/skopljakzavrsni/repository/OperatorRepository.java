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
        String sql = "SELECT operator.id, operator.username, operator.first_name, operator.last_name FROM operator WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return new Operator(id, rs.getString("username"), rs.getString("first_name"), rs.getString("last_name"));
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
        String sql = "SELECT operator.id, operator.username, operator.first_name, operator.last_name FROM operator WHERE 1 = 1";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()) {
                operators.add(new Operator(rs.getLong("id"), rs.getString("username"), rs.getString("first_name"), rs.getString("last_name")));
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
        String sql = "INSERT INTO operator (username, first_name, last_name) VALUES (LOWER(?), INITCAP(?), INITCAP(?)) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());

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

    public Operator findByUsername(String username){
        LOCK.lock();
        username = username.toLowerCase();
        String sql = "SELECT operator.id, operator.username, operator.first_name, operator.last_name FROM operator WHERE username = LOWER(?)";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return new Operator(rs.getLong("id"), rs.getString("username"), rs.getString("first_name"), rs.getString("last_name"));
                }else{
                    throw new EmptyResultSetException("Operator with username " + username + " not found");
                }
            }

        }catch(SQLException | RepositoryAccessException e){
            throw new RepositoryAccessException("Something went wrong while trying to find operator with username " + username + ". " + e.getMessage(), e);
        }finally{
            LOCK.unlock();
        }
    }

    @Override
    public void update(Operator entity) throws SQLException {
        LOCK.lock();
        String sql = "UPDATE operator SET username = LOWER(?), first_name = INITCAP(?), last_name = INITCAP(?) WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getUsername());
            ps.setString(2, entity.getFirstName());
            ps.setString(3, entity.getLastName());
            ps.setLong(4, entity.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new EmptyResultSetException("Update failed, no operator found with ID: " + entity.getId());
            }

        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {

    }

    @Override
    public List<Operator> saveAll(List<Operator> entities) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO operator (username, first_name, last_name) VALUES (LOWER(?), INITCAP(?), INITCAP(?)) RETURNING id";
        List<Operator> savedOperators = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            for (Operator entity : entities) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, entity.getUsername());
                    ps.setString(2, entity.getFirstName());
                    ps.setString(3, entity.getLastName());

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Long id = rs.getLong("id");
                            savedOperators.add(new Operator(
                                    id,
                                    entity.getUsername(),
                                    entity.getFirstName(),
                                    entity.getLastName()
                            ));
                        } else {
                            throw new EmptyResultSetException("Failed to insert operator with username: " + entity.getUsername());
                        }
                    }
                }
            }
            return savedOperators;
        } catch (RepositoryAccessException | SQLException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }

}
