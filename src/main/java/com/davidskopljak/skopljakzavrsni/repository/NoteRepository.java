package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Note;
import com.davidskopljak.skopljakzavrsni.exceptions.EmptyResultSetException;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class NoteRepository extends AbstractRepository<Note>{
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    @Override
    public Note findById(Long id) throws SQLException {
        return null;
    }

    @Override
    public List<Note> findAll() throws SQLException {
        return List.of();
    }

    @Override
    public Long save(Note entity) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO note (text, case_id, date_created) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, entity.getMessage());
            ps.setTimestamp(2, Timestamp.from(entity.getTimestamp()));
            ps.setString(3, entity.get());

            try(ResultSet rs = ps.executeQuery();){
                if (rs.next()) {
                    return rs.getLong("id");
                }else{
                    throw new EmptyResultSetException("No id retrieved for created location " + entity.getId() + ", possible issue with database");
                }
            }

        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }

    @Override
    public void update(Long id) throws SQLException {

    }

    @Override
    public void deleteById(Long id) throws SQLException {

    }

    @Override
    public void saveAll(List<Long> id) throws SQLException {

    }

    public List<Note> findAllById(Long id) throws SQLException {
        return new ArrayList<>();
    }
}
