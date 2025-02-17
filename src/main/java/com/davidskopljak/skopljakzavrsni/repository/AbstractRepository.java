package com.davidskopljak.skopljakzavrsni.repository;

import com.davidskopljak.skopljakzavrsni.entity.Entity;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractRepository <T> extends Entity {
    public abstract T findById(long id) throws SQLException;
    public abstract List<T> findAll() throws SQLException;
    public abstract Long save(T entity) throws SQLException;
}
