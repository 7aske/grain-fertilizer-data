package com._7aske.grain.data.dsl.token;

import com._7aske.grain.data.dsl.Operation;

public class OperationToken extends Token {
    private Operation operation;

    public OperationToken(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "OperationToken{" +
                "operation='" + operation + '\'' +
                '}';
    }
}
