package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.*;

public class FieldNode extends NavigableNode {
    private final String field;

    public FieldNode(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public <T> Expression<T> toPredicate(ArgumentEvaluator evaluator) {
        return evaluator.getRoot().get(field);
    }

    @Override
    public String toString() {
        return field;
    }
}
