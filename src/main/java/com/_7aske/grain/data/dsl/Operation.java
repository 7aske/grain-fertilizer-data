package com._7aske.grain.data.dsl;

import java.util.Arrays;
import java.util.List;

public enum Operation {
    AND("And"), OR("Or");

    public static final List<String> OPERATIONS = Arrays.stream(values())
            .map(Operation::getRepr)
            .toList();

    private final String repr;

    Operation(String repr) {
        this.repr = repr;
    }

    public String getRepr() {
        return repr;
    }
}
