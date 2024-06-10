package com._7aske.grain.data.meta;

public class EntityField {
    private final String columnName;
    private final String fieldName;
    private final Class<?> fieldType;
    private final EntityInformation entityInformation;
    private final boolean isCollection;

    public EntityField(String columnName, String fieldName, Class<?> fieldType, EntityInformation entityInformation, boolean isCollection) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.entityInformation = entityInformation;
        this.isCollection = isCollection;
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

    public EntityInformation getEntityInformation() {
        return entityInformation;
    }

    public boolean isCollection() {
        return isCollection;
    }
}
