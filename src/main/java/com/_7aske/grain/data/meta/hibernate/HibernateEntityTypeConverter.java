package com._7aske.grain.data.meta.hibernate;

import com._7aske.grain.data.meta.EntityField;
import com._7aske.grain.data.meta.EntityInformation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import org.hibernate.metamodel.model.domain.internal.ListAttributeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HibernateEntityTypeConverter {
    private final Map<Class<?>, EntityInformation> cache = new HashMap<>();
    private final Map<Attribute<?, ?>, EntityField> fieldCache = new HashMap<>();

    public EntityInformation convert(EntityManager entityManager, Class<?> entityClass) {
        if (cache.containsKey(entityClass)) {
            return cache.get(entityClass);
        }

        EntityType<?> entityType = entityManager.getMetamodel().entity(entityClass);


        EntityInformation entityInformation = new EntityInformation(entityType.getJavaType());
        entityInformation.setEntityName(entityType.getName());

        cache.put(entityClass, entityInformation);

        SingularAttribute<?, ?> id = entityType.getId(entityType.getIdType().getJavaType());
        entityInformation.setIdField(new EntityField(
                id.getName(),
                id.getName(),
                id.getJavaType(),
                id.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC
                        ? convert(entityManager, getAttributeType(id))
                        : null,
                id.isCollection()
        ));

        List<EntityField> entityFields =
                entityType.getDeclaredAttributes()
                        .stream()
                        .map(attr -> {
                            if (fieldCache.containsKey(attr)) {
                                return fieldCache.get(attr);
                            }

                            EntityField entityField = new EntityField(
                                    attr.getName(),
                                    attr.getName(),
                                    getAttributeType(attr),
                                    attr.getPersistentAttributeType() != Attribute.PersistentAttributeType.BASIC
                                            ? convert(entityManager, getAttributeType(attr))
                                            : null,
                                    attr.isCollection()
                            );

                            fieldCache.put(attr, entityField);

                            return entityField;
                        })
                        .toList();

        entityInformation.setEntityFields(entityFields);

        return entityInformation;
    }

    private Class<?> getAttributeType(Attribute<?, ?> attr) {
        if (attr.isCollection() && attr instanceof ListAttributeImpl listAttribute) {
            return listAttribute.getElementType().getJavaType();
        } else {
            return attr.getJavaType();
        }
    }
}
