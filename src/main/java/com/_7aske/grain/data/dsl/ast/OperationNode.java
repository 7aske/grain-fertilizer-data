package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.Operation;
import jakarta.persistence.criteria.*;

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
    public <T> Expression<T> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder builder) {
        switch (operation) {
            case AND:
                return (Expression<T>) (builder.and((Expression<Boolean>) left.toPredicate(root, query, builder), (Expression<Boolean>) right.toPredicate(root, query, builder)));
            case OR:
                return (Expression<T>) builder.or((Expression<Boolean>) left.toPredicate(root, query, builder), (Expression<Boolean>) right.toPredicate(root, query, builder));
            case EQUALS:
                return (Expression<T>) builder.equal(left.toPredicate(root, query, builder), right.toPredicate(root, query, builder));
            case NOT:
                return (Expression<T>) builder.not((Expression<Boolean>) left.toPredicate(root, query, builder));
//            case GREATER_THAN:
//                return (Expression<T>) builder.greaterThan(left.toPredicate(root, query, builder), right.toPredicate(root, query, builder));
//            case LESS_THAN:
//                return (Expression<T>) builder.lessThan(left.toPredicate(root, query, builder), right.toPredicate(root, query, builder));
//            case GREATER_THAN_EQUALS:
//                return (Expression<T>) builder.greaterThanOrEqualTo(left.toPredicate(root, query, builder), right.toPredicate(root, query, builder));
//            case LESS_THAN_EQUALS:
//                return (Expression<T>) builder.lessThanOrEqualTo(left.toPredicate(root, query, builder), right.toPredicate(root, query, builder));
            case LIKE, STARTS_WITH, ENDS_WITH:
                return (Expression<T>) builder.like((Expression<String>) left.toPredicate(root, query, builder), (Expression<String>) right.toPredicate(root, query, builder));
            case IN:
                return (Expression<T>) left.toPredicate(root, query, builder).in(right.toPredicate(root, query, builder));
            case IS_NULL:
                return (Expression<T>) builder.isNull(left.toPredicate(root, query, builder));
//            case BETWEEN:
//                return (Expression<T>) builder.between(left.toPredicate(root, query, builder), right.toPredicate(root, query, builder));
            case CONTAINS:
                return (Expression<T>) builder.isMember(right.toPredicate(root, query, builder), (Expression<Collection<T>>) left.toPredicate(root, query, builder));
            case IS_EMPTY:
                return (Expression<T>) builder.isEmpty((Expression<? extends Collection<?>>) left.toPredicate(root, query, builder));
            case IS_TRUE:
                return (Expression<T>) builder.isTrue((Expression<Boolean>) left.toPredicate(root, query, builder));
            case IS_FALSE:
                return (Expression<T>) builder.isFalse((Expression<Boolean>) left.toPredicate(root, query, builder));
            default:
                throw new IllegalArgumentException();
        }
    }
}
