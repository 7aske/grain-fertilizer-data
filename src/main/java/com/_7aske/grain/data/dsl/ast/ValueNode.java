package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.Expression;

public record ValueNode(int argIndex) implements Node {
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
