package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Note;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteRepository extends AbstractRepository<Note>{

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
        return 0L;
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

    public ArrayList<Note> findAllById(Long id) throws SQLException {
        return new ArrayList<>();
    }
}
