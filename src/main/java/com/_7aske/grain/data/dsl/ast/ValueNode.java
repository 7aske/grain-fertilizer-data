package com._7aske.grain.data.dsl.ast;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

public class ValueNode extends Node {
    private final Object value;

    public ValueNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Expression<T> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder builder) {
        return (Expression<T>) builder.literal(value);
    }
}
