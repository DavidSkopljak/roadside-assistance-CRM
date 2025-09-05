package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EntityBuffer<U, T>{
    private File bufferFile;

    public EntityBuffer(File bufferFile){
        this.bufferFile = bufferFile;
    }

    public void writeEntity(BufferableEntity<U, T> entity) {
        boolean fileExists = bufferFile.exists();

        try (FileOutputStream fos = new FileOutputStream(bufferFile, true);
             ObjectOutputStream oos = fileExists
                     ? new AppendableObjectOutputStream(fos)
                     : new ObjectOutputStream(fos)
        ) {
            oos.writeObject(entity);
        } catch (IOException e) {
            CRMApplication.log.error("Could not write entity to buffer file", e);
        }
    }

    public void clearBuffer() {}

    public Map<BufferedChangeType, Map<U, BufferableEntity<U, T>>> getBufferedEntities() {
        Map<U, BufferableEntity<U, T>> newEntityMap = new HashMap<>();
        Map<U, BufferableEntity<U, T>> updatedEntityMap = new HashMap<>();
        Map<U, BufferableEntity<U, T>> deletedEntityMap = new HashMap<>();

        Map<BufferedChangeType, Map<U, BufferableEntity<U, T>>> allEntitiesMap = new HashMap<>();

        if (!bufferFile.exists()) return allEntitiesMap;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bufferFile))) {
            while (true) {
                try {
                    BufferableEntity<U, T> entity = (BufferableEntity<U, T>) ois.readObject();
                    BufferedChangeType type = entity.getType();
                    switch(type){
                        case BufferedChangeType.NEW: newEntityMap.put(entity.getKey(), entity); break;
                        case BufferedChangeType.UPDATED: updatedEntityMap.put(entity.getKey(), entity); break;
                        case BufferedChangeType.DELETED: deletedEntityMap.put(entity.getKey(), entity); break;
                        default: break;
                    }

                } catch (EOFException eof) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        allEntitiesMap.put(BufferedChangeType.NEW, newEntityMap);
        allEntitiesMap.put(BufferedChangeType.UPDATED, updatedEntityMap);
        allEntitiesMap.put(BufferedChangeType.DELETED, deletedEntityMap);

        return allEntitiesMap;
    }
}
