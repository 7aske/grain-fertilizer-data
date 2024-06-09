package com._7aske.grain.data.dsl;

public enum Operation {
    AND("And", 1),
    OR("Or", 1),

    EQUALS("Equals", 4),
    GREATER_THAN("GreaterThan", 4),
    LESS_THAN("LessThan", 4),
    GREATER_THAN_EQUALS("GreaterThanEquals", 4),
    LESS_THAN_EQUALS("LessThanEquals", 4),
    LIKE("Like", 4),
    IN("In", 4),
    CONTAINS("Contains", 3),
    STARTS_WITH("StartsWith", 4),
    ENDS_WITH("EndsWith", 4),

    NOT("Not", 3),
    IS_NULL("IsNull", 3),
    IS_EMPTY("IsEmpty", 3),
    IS_TRUE("IsTrue", 3),
    IS_FALSE("IsFalse", 3);
//    BETWEEN("Between"),

    private final String repr;
    private final int precedence;

    Operation(String repr, int precedence) {
        this.repr = repr;
        this.precedence = precedence;
    }

    public String getRepr() {
        return repr;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isUnary() {
        return this == NOT || this == IS_NULL || this == IS_EMPTY || this == IS_TRUE || this == IS_FALSE;
    }

    public boolean isLogical() {
        return this == AND || this == OR;
    }

    public boolean isLiteral() {
        return !isUnary() && !isLogical();
    }
}
