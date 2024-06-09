package com._7aske.grain.data.dsl.ast;

import jakarta.persistence.criteria.*;

public class FieldNode extends Node {
    private final String field;

    public FieldNode(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public <T> Expression<T> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder builder) {
        return root.get(field);
    }

    @Override
    public String toString() {
        return field;
    }
}
