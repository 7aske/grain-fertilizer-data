package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;

public class JoinNode extends NavigableNode {
    private final NavigableNode parent;
    private final String field;

    public JoinNode(NavigableNode parent, String field) {
        this.parent = parent;
        this.field = field;
    }

    public NavigableNode getParent() {
        return parent;
    }

    public String getField() {
        return field;
    }

    @Override
    public <T> Expression<T> toExpression(ArgumentEvaluator evaluator) {
        return ((Path<?>) getParent().toExpression(evaluator)).get(getField());
    }
}
