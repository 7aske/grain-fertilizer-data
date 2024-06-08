package com._7aske.grain.data.dsl.token;

import com._7aske.grain.data.dsl.RootOperation;

public class RootOperationToken extends Token {
    private final RootOperation rootOperation;

    public RootOperationToken(RootOperation rootOperation) {
        this.rootOperation = rootOperation;
    }

    public RootOperation getRootOperation() {
        return rootOperation;
    }

    @Override
    public String toString() {
        return rootOperation.toString();
    }
}
