package com.davidskopljak.skopljakzavrsni.controller;

import com.davidskopljak.skopljakzavrsni.entity.BufferableEntity;
import com.davidskopljak.skopljakzavrsni.entity.Case;
import com.davidskopljak.skopljakzavrsni.entity.EntityBuffer;
import com.davidskopljak.skopljakzavrsni.enums.BufferedChangeType;
import com.davidskopljak.skopljakzavrsni.exceptions.RepositoryAccessException;
import com.davidskopljak.skopljakzavrsni.helpers.MiscHelpers;
import com.davidskopljak.skopljakzavrsni.repository.CaseRepository;
import javafx.scene.control.Alert;

import java.util.Map;

public class CaseDiffMenuController {
    public void handleSaveChanges(){
        EntityBuffer<Long, Case> caseBuffer = CRMApplication.getCaseBuffer();
        Map<BufferedChangeType, Map<Long, BufferableEntity<Long, Case>>> caseDiffs = caseBuffer.getBufferedEntities();

        CaseRepository caseRepository = new CaseRepository();

        for (Map<Long, BufferableEntity<Long, Case>> entitiesMap : caseDiffs.values()) {
            for (BufferableEntity<Long, Case> bufferableEntity : entitiesMap.values()) {
                Case c = bufferableEntity.getEntity();
                if (c == null) {
                    MiscHelpers.showAlert("Failed to save changes", Alert.AlertType.ERROR);
                    return;
                }

                try {
                    if (c.getId() == null) {
                        caseRepository.save(c);
                    } else {
                        caseRepository.update(c);
                    }
                } catch (RepositoryAccessException e) {
                    CRMApplication.log.error("Error saving or updating case " + c.getId(), e);
                }
            }
        }

        CRMApplication.getCaseBuffer().clearBuffer();
    }

    public void handleExit(){
        MiscHelpers.loadScene("main-view.fxml", "View cases", CRMApplication.getPrimaryStage());
    }
}
