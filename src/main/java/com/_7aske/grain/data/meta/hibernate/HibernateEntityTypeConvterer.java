package com._7aske.grain.data.meta.hibernate;

import com._7aske.grain.data.meta.EntityField;
import com._7aske.grain.data.meta.EntityInformation;
import jakarta.persistence.metamodel.EntityType;

import java.util.ArrayList;
import java.util.List;

public class HibernateEntityTypeConvterer {
    public EntityInformation convert(EntityType<?> entityClass) {
        EntityInformation entityInformation = new EntityInformation(entityClass.getJavaType());
        entityInformation.setEntityName(entityClass.getName());
        List<EntityField> entityFields = new ArrayList<>();
        entityClass.getDeclaredAttributes().forEach(attribute -> {
            entityFields.add(new EntityField(
                    attribute.getName(),
                    attribute.getName(),
                    attribute.getJavaType()
            ));
        });
        entityInformation.setEntityFields(entityFields);
        entityInformation.setIdField(new EntityField(
                entityClass.getId(entityClass.getIdType().getJavaType()).getName(),
                entityClass.getId(entityClass.getIdType().getJavaType()).getName(),
                entityClass.getId(entityClass.getIdType().getJavaType()).getJavaType()
        ));
        return entityInformation;
    }
}
