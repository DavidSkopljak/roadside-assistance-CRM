package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BufferableEntity <U, T> implements Serializable {
    private T entity;
    private U key;
    private LocalDateTime lastUpdated;
    private Operator lastUpdatedBy;
    private BufferedChangeType type;

    public BufferableEntity(U key,T entity, Operator lastUpdatedBy, BufferedChangeType type) {
        this.key = key;
        this.entity = entity;
        this.lastUpdated = LocalDateTime.now();
        this.lastUpdatedBy = lastUpdatedBy;
        this.type = type;
    }

    public T getEntity() {
        return entity;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Operator getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public BufferedChangeType getType() {
        return type;
    }

    public U getKey() {return key;}
}
