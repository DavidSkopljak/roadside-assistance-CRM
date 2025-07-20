package com.davidskopljak.skopljakzavrsni.repository;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractRepository <T>{
    public abstract T findById(Long id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    public abstract Long save(T entity) throws SQLException;
    public abstract void update(T entity) throws SQLException;
    public abstract void deleteById(Long id) throws SQLException;
    public abstract List<T> saveAll(List<T> entities) throws SQLException;
}
