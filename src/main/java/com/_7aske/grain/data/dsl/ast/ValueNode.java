package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

import java.util.Collection;

public class ValueNode extends Node {
    private final int argIndex;

    public ValueNode(int argIndex) {
        this.argIndex = argIndex;
    }

    public int getArgIndex() {
        return argIndex;
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> Expression<T> toExpression(ArgumentEvaluator evaluator) {
        Object value = evaluator.evaluate(argIndex);
        return (Expression<T>) evaluator.getBuilder().literal(value);
    }

    public Object getValue(ArgumentEvaluator evaluator) {
        return evaluator.evaluate(argIndex);
    }
}
