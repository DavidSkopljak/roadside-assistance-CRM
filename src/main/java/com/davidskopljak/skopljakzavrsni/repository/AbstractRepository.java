package com.davidskopljak.skopljakzavrsni.repository;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractRepository <T>{
    public abstract T findById(Long id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    public abstract Long save(T entity) throws SQLException;
}
