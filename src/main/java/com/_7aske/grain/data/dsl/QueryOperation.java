package com._7aske.grain.data.dsl;

import java.util.Arrays;
import java.util.List;

public enum QueryOperation {
    EQUALS("Equals"),
    NOT("Not"),
    GREATER_THAN("GreaterThan"),
    LESS_THAN("LessThan"),
    GREATER_THAN_EQUALS("GreaterThanEquals"),
    LESS_THAN_EQUALS("LessThanEquals"),
    LIKE("Like"),
    IN("In"),
    IS_NULL("IsNull"),
    BETWEEN("Between"),
    CONTAINS("Contains"),
    STARTS_WITH("StartsWith"),
    ENDS_WITH("EndsWith"),
    IS_EMPTY("IsEmpty"),
    IS_NOT_EMPTY("IsNotEmpty"),
    IS_TRUE("IsTrue"),
    IS_FALSE("IsFalse");

    public static final List<String> QUERY_OPERATIONS = Arrays.stream(values())
            .map(QueryOperation::getRepr)
            .toList();

    private final String repr;

    QueryOperation(String repr) {
        this.repr = repr;
    }

    public String getRepr() {
        return repr;
    }
}
