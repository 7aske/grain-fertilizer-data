package com._7aske.grain.data.dsl;

import java.util.Arrays;
import java.util.List;

public enum RootOperation {
    FIND_BY("findBy"),
    FIND_ALL_BY("findAllBy"),
    DELETE_BY("deleteBy"),
    DELETE_ALL_BY("deleteAllBy"),
    COUNT_BY("countBy"),
    EXISTS_BY("existsBy");

    public static final List<String> ROOT_OPERATIONS = Arrays.stream(values())
            .map(RootOperation::getRepr)
            .toList();

    private final String repr;

    RootOperation(String repr) {
        this.repr = repr;
    }

    public String getRepr() {
        return repr;
    }
}
