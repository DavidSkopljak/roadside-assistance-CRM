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
            ps.setString(3, entity.getMessage());

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
    public void update(Note entity) throws SQLException {
        LOCK.lock();
        String sql = "UPDATE note SET text = ?, case_id = ?, date_created = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getMessage());
            ps.setLong(2, entity.getCaseId());
            ps.setTimestamp(3, Timestamp.from(entity.getTimestamp()));
            ps.setLong(4, entity.getId());

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new EmptyResultSetException("No note updated for ID: " + entity.getId());
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
    public List<Note> saveAll(List<Note> entities) throws SQLException {
        LOCK.lock();
        String sql = "INSERT INTO note (text, case_id, date_created) VALUES (?, ?, ?) RETURNING id";
        List<Note> savedNotes = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection()) {
            for (Note entity : entities) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, entity.getMessage());
                    ps.setLong(2, entity.getCaseId());
                    ps.setTimestamp(3, Timestamp.from(entity.getTimestamp()));

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Note savedNote = new Note(
                                    rs.getLong("id"),
                                    entity.getMessage(),
                                    entity.getTimestamp(),
                                    entity.getCaseId()
                            );
                            savedNotes.add(savedNote);
                        } else {
                            throw new EmptyResultSetException("No ID retrieved for a created note, possible issue with database");
                        }
                    }
                }
            }
            return savedNotes;
        } catch (RepositoryAccessException e) {
            throw new RepositoryAccessException(e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
    }


    public List<Note> findAllByCaseId(Long caseId) throws SQLException {
        LOCK.lock();
        List<Note> notes = new ArrayList<>();

        String sql = "SELECT text, case_id, date_created FROM note WHERE case_id = ?";
        try (Connection conn = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1, caseId);

            try(ResultSet rs = ps.executeQuery();){
                while (rs.next()) {
                    notes.add(new Note(rs.getLong("id"), rs.getString("text"), rs.getTimestamp("date_created").toInstant(), rs.getLong("case_id")));
                }

                if (notes.isEmpty()) {
                    return List.of();
                }else{
                    return notes;
                }
            }
        }catch(RepositoryAccessException e){
            throw new RepositoryAccessException(e.getMessage(), e);
        }finally {
            LOCK.unlock();
        }
    }
}
