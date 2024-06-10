package com._7aske.grain.data.meta;

import java.util.List;

public class EntityInformation {
    private String entityName;
    private Class<?> entityClass;
    private List<EntityField> entityFields;
    private EntityField idField;

    public EntityInformation(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public boolean hasField(String fieldName) {
        return entityFields.stream()
                .anyMatch(field -> field.getFieldName().equals(fieldName));
    }

    public boolean hasField(String join, String fieldName) {
        if (join == null) {
            return hasField(fieldName);
        }

        return entityFields.stream().filter(field -> field.getFieldName().equals(join))
                .findFirst()
                .filter(field -> field.getEntityInformation() != null)
                .map(field -> field.getEntityInformation().hasField(fieldName))
                .orElse(false);
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public List<EntityField> getEntityFields() {
        return entityFields;
    }

    public void setEntityFields(List<EntityField> entityFields) {
        this.entityFields = entityFields;
    }

    public EntityField getIdField() {
        return idField;
    }

    public void setIdField(EntityField idField) {
        this.idField = idField;
    }
}
