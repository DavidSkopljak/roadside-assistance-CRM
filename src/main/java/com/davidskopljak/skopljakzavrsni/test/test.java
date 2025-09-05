package com.davidskopljak.skopljakzavrsni.test;

import com.davidskopljak.skopljakzavrsni.entity.BufferableEntity;
import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.entity.EntityBuffer;
import com.davidskopljak.skopljakzavrsni.entity.Operator;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;

import java.io.*;
import java.sql.SQLException;
import java.util.Map;

public class test {
    public static void main(String[] args) throws SQLException {
        CaseRepository caseRepository = new CaseRepository();
        EntityBuffer<Long, Case> caseBuffer = new EntityBuffer<>(new File("cases.dat"));

        // Clean up existing buffer file for fresh start
        File bufferFile = new File("cases.dat");
        if (bufferFile.exists()) {
            bufferFile.delete();
            System.out.println("Existing buffer file cleared for fresh test\n");
        }

        // Get the same case to use for all buffer types
        Case caze = caseRepository.findById(Long.valueOf(69));
        if (caze == null) {
            System.err.println("Case with ID 69 not found!");
            return;
        }

        System.out.println("=== Writing Same Case with Different Buffer Types ===");
        System.out.println("Using Case ID: " + caze.getId() + " - " + caze.getDamageDescription());

        // Create different operators for different change types
        Operator newOperator = new Operator("john_doe", "John", "Doe");
        Operator updateOperator = new Operator("jane_smith", "Jane", "Smith");
        Operator deleteOperator = new Operator("admin_user", "Admin", "User");

        try {
            // Write NEW entry
            System.out.println("\nWriting NEW entry:");
            caseBuffer.writeEntity(new BufferableEntity<>(
                    caze.getId(),
                    caze,
                    newOperator,
                    BufferedChangeType.NEW
            ));
            System.out.println("  ✓ Added NEW case ID: " + caze.getId());

            // Write UPDATED entry (same case, different buffer type)
            System.out.println("\nWriting UPDATED entry:");
            caseBuffer.writeEntity(new BufferableEntity<>(
                    caze.getId() + 1000, // Different key to avoid overwriting
                    caze,
                    updateOperator,
                    BufferedChangeType.UPDATED
            ));
            System.out.println("  ✓ Added UPDATED case ID: " + (caze.getId() + 1000));

            // Write DELETED entry (same case, different buffer type)
            System.out.println("\nWriting DELETED entry:");
            caseBuffer.writeEntity(new BufferableEntity<>(
                    caze.getId() + 2000, // Different key to avoid overwriting
                    caze,
                    deleteOperator,
                    BufferedChangeType.DELETED
            ));
            System.out.println("  ✓ Added DELETED case ID: " + (caze.getId() + 2000));

        } catch (Exception e) {
            System.err.println("Error writing entities: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("=== Reading All Entities from Buffer ===");

        try {
            // Read all entities from buffer
            Map<BufferedChangeType, Map<Long, BufferableEntity<Long, Case>>> bufferedCases =
                    caseBuffer.getBufferedEntities();

            System.out.println("Total buffer types found: " + bufferedCases.size());

            // Display entities by each buffer type
            for (BufferedChangeType changeType : BufferedChangeType.values()) {
                System.out.println("\n" + "-".repeat(40));
                System.out.println("BUFFER TYPE: " + changeType);
                System.out.println("-".repeat(40));

                Map<Long, BufferableEntity<Long, Case>> entitiesOfType = bufferedCases.get(changeType);

                if (entitiesOfType == null || entitiesOfType.isEmpty()) {
                    System.out.println("  No entities found for type: " + changeType);
                    continue;
                }

                System.out.println("  Found " + entitiesOfType.size() + " entities:");

                for (Map.Entry<Long, BufferableEntity<Long, Case>> entry : entitiesOfType.entrySet()) {
                    Long key = entry.getKey();
                    BufferableEntity<Long, Case> bufferableEntity = entry.getValue();

                    if (bufferableEntity != null && bufferableEntity.getEntity() != null) {
                        Case bufferedCase = bufferableEntity.getEntity();
                        Operator operator = bufferableEntity.getEntity().getFirstOperator();

                        System.out.println("    Key: " + key);
                        System.out.println("    Case ID: " + bufferedCase.getId());
                        System.out.println("    Damage: " + bufferedCase.getDamageDescription());
                        System.out.println("    State: " + bufferedCase.getState());
                        System.out.println("    Operator: " + operator.getFirstName() + " " + operator.getLastName() +
                                " (" + operator.getUsername() + ")");
                        System.out.println("    Change Type: " + bufferableEntity.getType());
                        System.out.println();
                    } else {
                        System.out.println("    Key: " + key + " - NULL ENTITY");
                    }
                }
            }

            // Summary statistics
            System.out.println("=".repeat(60));
            System.out.println("=== SUMMARY ===");
            int totalEntities = 0;
            for (Map<Long, BufferableEntity<Long, Case>> typeMap : bufferedCases.values()) {
                totalEntities += typeMap.size();
            }
            System.out.println("Total entities in buffer: " + totalEntities);
            System.out.println("Buffer file size: " + bufferFile.length() + " bytes");
            System.out.println("Buffer file location: " + bufferFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Error reading entities: " + e.getMessage());
            e.printStackTrace();
        }
    }
}