package com._7aske.grain.data.dsl.token;

import com._7aske.grain.data.dsl.Operation;

public record OperationToken(Operation operation) implements Token {
    @Override
    public String toString() {
        return "OperationToken{" +
               "operation='" + operation + '\'' +
               '}';
    }
}
