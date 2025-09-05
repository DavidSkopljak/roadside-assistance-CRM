package com.davidskopljak.skopljakzavrsni.entity;

public class FieldDiff {
    private String fieldName;
    private Object originalValue;
    private Object modifiedValue;

    public FieldDiff(String fieldName, Object originalValue, Object modifiedValue) {
        this.fieldName = fieldName;
        this.originalValue = originalValue;
        this.modifiedValue = modifiedValue;
    }

    public String getFieldName() { return fieldName; }
    public Object getOriginalValue() { return originalValue; }
    public Object getModifiedValue() { return modifiedValue; }
}