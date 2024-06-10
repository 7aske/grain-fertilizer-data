package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import com._7aske.grain.data.dsl.Operation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

import java.util.Collection;


public class OperationNode extends BinaryNode {
    public OperationNode(Operation operation) {
        super(null, operation, null);
    }

    public OperationNode(Node left, Operation operation, Node right) {
        super(left, operation, right);
    }

    @Override
    public String toString() {
        return "(%s %s %s)".formatted(left, operation, right);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Expression<T> toPredicate(ArgumentEvaluator evaluator) {
        switch (operation) {
            case AND:
                return (Expression<T>) (evaluator.getBuilder().and(left.toPredicate(evaluator), right.toPredicate(evaluator)));
            case OR:
                return (Expression<T>) evaluator.getBuilder().or(left.toPredicate(evaluator), right.toPredicate(evaluator));
            case EQUALS:
                return (Expression<T>) evaluator.getBuilder().equal(left.toPredicate(evaluator), right.toPredicate(evaluator));
            case NOT:
                return (Expression<T>) evaluator.getBuilder().not(left.toPredicate(evaluator));
            case GREATER_THAN:
                return (Expression<T>) evaluator.getBuilder().greaterThan(left.toPredicate(evaluator), right.toPredicate(evaluator));
            case LESS_THAN:
                return (Expression<T>) evaluator.getBuilder().lessThan(left.toPredicate(evaluator), right.toPredicate(evaluator));
            case GREATER_THAN_EQUALS:
                return (Expression<T>) evaluator.getBuilder().greaterThanOrEqualTo(left.toPredicate(evaluator), right.toPredicate(evaluator));
            case LESS_THAN_EQUALS:
                return (Expression<T>) evaluator.getBuilder().lessThanOrEqualTo(left.toPredicate(evaluator), right.toPredicate(evaluator));
            case LIKE:
                return (Expression<T>) evaluator.getBuilder().like(left.toPredicate(evaluator), wrapLike(evaluator.getBuilder(), right.toPredicate(evaluator)));
            case STARTS_WITH:
                return (Expression<T>) evaluator.getBuilder().like(left.toPredicate(evaluator), wrapStartsWith(evaluator.getBuilder(), right.toPredicate(evaluator)));
            case ENDS_WITH:
                return (Expression<T>) evaluator.getBuilder().like(left.toPredicate(evaluator), wrapEndsWith(evaluator.getBuilder(), right.toPredicate(evaluator)));
            case IN:
                return (Expression<T>) left.toPredicate((evaluator)).in(right.toPredicate(evaluator));
            case IS_NULL:
                return (Expression<T>) evaluator.getBuilder().isNull(left.toPredicate(evaluator));
            case CONTAINS:
                return (Expression<T>) evaluator.getBuilder().isMember(right.toPredicate(evaluator), left.<Collection<T>>toPredicate(evaluator));
            case IS_EMPTY:
                return (Expression<T>) evaluator.getBuilder().isEmpty(left.toPredicate(evaluator));
            case IS_TRUE:
                return (Expression<T>) evaluator.getBuilder().isTrue(left.toPredicate(evaluator));
            case IS_FALSE:
                return (Expression<T>) evaluator.getBuilder().isFalse(left.toPredicate(evaluator));
            default:
                throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Expression<T> wrapLike(CriteriaBuilder builder , Expression<?> expression) {
        return (Expression<T>) builder.concat(builder.concat("%", (Expression<String>) expression), "%");
    }

    @SuppressWarnings("unchecked")
    private <T> Expression<T> wrapStartsWith(CriteriaBuilder builder , Expression<?> expression) {
        return (Expression<T>) builder.concat((Expression<String>) expression, "%");
    }

    @SuppressWarnings("unchecked")
    private <T> Expression<T> wrapEndsWith(CriteriaBuilder builder , Expression<?> expression) {
        return (Expression<T>) builder.concat("%", (Expression<String>) expression);
    }

}
