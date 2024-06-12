package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;

public record JoinNode(NavigableNode parent,
                       String field) implements NavigableNode {
    @Override
    public <T> Expression<T> toExpression(ArgumentEvaluator evaluator) {
        return ((Path<?>) parent().toExpression(evaluator)).get(field());
    }
}
