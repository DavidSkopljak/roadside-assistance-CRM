package com.davidskopljak.skopljakzavrsni.helpers;

import com.davidskopljak.skopljakzavrsni.entity.Note;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JSONParser {
    public static List<Note> parseCaseNotes(String caseNotesJson) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Add this line
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            System.out.println("case notes json in JSONParser: " + caseNotesJson);
            return mapper.readValue(caseNotesJson, mapper.getTypeFactory().constructCollectionType(List.class, Note.class));
        } catch (Exception e) {
            throw new SQLException("Error parsing case notes JSON", e);
        }
    }
}
