package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.*;

public record FieldNode(String field) implements NavigableNode {
    @Override
    public <T> Expression<T> toExpression(ArgumentEvaluator evaluator) {
        return evaluator.getRoot().get(field);
    }

    @Override
    public String toString() {
        return field;
    }
}
