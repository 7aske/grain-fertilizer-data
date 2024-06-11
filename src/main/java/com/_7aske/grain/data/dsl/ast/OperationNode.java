package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import com._7aske.grain.data.dsl.Operation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;

import java.util.Collection;


public class OperationNode extends BinaryNode {
    public OperationNode(Node left, Operation operation, Node right) {
        super(left, operation, right);
    }

    @Override
    public String toString() {
        return "(%s %s %s)".formatted(left, operation, right);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Expression<T> toExpression(ArgumentEvaluator evaluator) {
        switch (operation) {
            case AND:
                return (Expression<T>) (evaluator.getBuilder().and(left.toExpression(evaluator), right.toExpression(evaluator)));
            case OR:
                return (Expression<T>) evaluator.getBuilder().or(left.toExpression(evaluator), right.toExpression(evaluator));
            case EQUALS:
                return (Expression<T>) evaluator.getBuilder().equal(left.toExpression(evaluator), right.toExpression(evaluator));
            case NOT:
                return (Expression<T>) evaluator.getBuilder().not(left.toExpression(evaluator));
            case GREATER_THAN:
                return (Expression<T>) evaluator.getBuilder().greaterThan(left.toExpression(evaluator), right.toExpression(evaluator));
            case LESS_THAN:
                return (Expression<T>) evaluator.getBuilder().lessThan(left.toExpression(evaluator), right.toExpression(evaluator));
            case GREATER_THAN_EQUALS:
                return (Expression<T>) evaluator.getBuilder().greaterThanOrEqualTo(left.toExpression(evaluator), right.toExpression(evaluator));
            case LESS_THAN_EQUALS:
                return (Expression<T>) evaluator.getBuilder().lessThanOrEqualTo(left.toExpression(evaluator), right.toExpression(evaluator));
            case LIKE:
                return (Expression<T>) evaluator.getBuilder().like(left.toExpression(evaluator), wrapLike(evaluator.getBuilder(), right.toExpression(evaluator)));
            case STARTS_WITH:
                return (Expression<T>) evaluator.getBuilder().like(left.toExpression(evaluator), wrapStartsWith(evaluator.getBuilder(), right.toExpression(evaluator)));
            case ENDS_WITH:
                return (Expression<T>) evaluator.getBuilder().like(left.toExpression(evaluator), wrapEndsWith(evaluator.getBuilder(), right.toExpression(evaluator)));
            case IN:
                if (right instanceof ValueNode valueNode) {
                    return (Expression<T>) evaluator.getBuilder().in(left.toExpression(evaluator)).value(valueNode.getValue(evaluator));
                } else {
                    return (Expression<T>) evaluator.getBuilder().in(left.toExpression(evaluator)).value(right.toExpression(evaluator));
                }
            case IS_NULL:
                return (Expression<T>) evaluator.getBuilder().isNull(left.toExpression(evaluator));
            case CONTAINS:
                if (right instanceof ValueNode valueNode) {
                    return (Expression<T>) evaluator.getBuilder().isMember(valueNode.getValue(evaluator), left.toExpression(evaluator));
                } else {
                    return (Expression<T>) evaluator.getBuilder().isMember(right.toExpression(evaluator), left.<Collection<T>>toExpression(evaluator));
                }
            case IS_EMPTY:
                return (Expression<T>) evaluator.getBuilder().isEmpty(left.toExpression(evaluator));
            case IS_TRUE:
                return (Expression<T>) evaluator.getBuilder().isTrue(left.toExpression(evaluator));
            case IS_FALSE:
                return (Expression<T>) evaluator.getBuilder().isFalse(left.toExpression(evaluator));
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
