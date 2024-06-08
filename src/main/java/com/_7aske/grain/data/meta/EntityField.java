package com._7aske.grain.data.meta;

public class EntityField {
    private final String columnName;
    private final String fieldName;
    private final Class<?> fieldType;

    public EntityField(String columnName, String fieldName, Class<?> fieldType) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }
}
