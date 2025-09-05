package com.davidskopljak.skopljakzavrsni.entity;

import com.davidskopljak.skopljakzavrsni.controller.CRMApplication;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class EntityBuffer<U, T>{
    private File bufferFile;
    private static ReentrantLock lock = new ReentrantLock();

    public EntityBuffer(File bufferFile){
        this.bufferFile = bufferFile;
    }

    public void writeEntity(BufferableEntity<U, T> entity) {
        lock.lock();

        boolean fileExists = bufferFile.exists();

        try (FileOutputStream fos = new FileOutputStream(bufferFile, true);
             ObjectOutputStream oos = fileExists
                     ? new AppendableObjectOutputStream(fos)
                     : new ObjectOutputStream(fos)
        ) {
            oos.writeObject(entity);
        } catch (IOException e) {
            CRMApplication.log.error("Could not write entity to buffer file", e);
        } finally{
            lock.unlock();
        }
    }

    public boolean clearBuffer() {
        Path path = Path.of(CRMApplication.getCaseBufferFilePath());
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            CRMApplication.log.error("Failed to delete binary file", e);
            return false;
        }    }

    public Map<BufferedChangeType, Map<U, BufferableEntity<U, T>>> getBufferedEntities() {
        lock.lock();
        try {
            Map<U, BufferableEntity<U, T>> newEntityMap = new HashMap<>();
            Map<U, BufferableEntity<U, T>> updatedEntityMap = new HashMap<>();
            Map<U, BufferableEntity<U, T>> deletedEntityMap = new HashMap<>();
            Map<BufferedChangeType, Map<U, BufferableEntity<U, T>>> allEntitiesMap = new HashMap<>();

            if (!bufferFile.exists()) return allEntitiesMap;

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(bufferFile))) {
                readEntitiesFromStream(ois, newEntityMap, updatedEntityMap, deletedEntityMap);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            allEntitiesMap.put(BufferedChangeType.NEW, newEntityMap);
            allEntitiesMap.put(BufferedChangeType.UPDATED, updatedEntityMap);
            allEntitiesMap.put(BufferedChangeType.DELETED, deletedEntityMap);

            return allEntitiesMap;
        } finally {
            lock.unlock();
        }
    }

    private void readEntitiesFromStream(
            ObjectInputStream ois,
            Map<U, BufferableEntity<U, T>> newEntityMap,
            Map<U, BufferableEntity<U, T>> updatedEntityMap,
            Map<U, BufferableEntity<U, T>> deletedEntityMap
    ) throws IOException, ClassNotFoundException {
        while (true) {
            try {
                BufferableEntity<U, T> entity = (BufferableEntity<U, T>) ois.readObject();
                BufferedChangeType type = entity.getType();
                switch (type) {
                    case NEW: newEntityMap.put(entity.getKey(), entity); break;
                    case UPDATED: updatedEntityMap.put(entity.getKey(), entity); break;
                    case DELETED: deletedEntityMap.put(entity.getKey(), entity); break;
                    default: break;
                }
            } catch (EOFException _) {
                break;
            }
        }
    }

}
