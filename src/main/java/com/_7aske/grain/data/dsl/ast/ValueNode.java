package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

public class ValueNode extends Node {
    private final int argIndex;

    public ValueNode(int argIndex) {
        this.argIndex = argIndex;
    }

    @SuppressWarnings("unchecked")
    public <T> Expression<T> toPredicate(ArgumentEvaluator evaluator) {
        return (Expression<T>) evaluator.getBuilder().literal(evaluator.evaluate(argIndex));
    }
}
