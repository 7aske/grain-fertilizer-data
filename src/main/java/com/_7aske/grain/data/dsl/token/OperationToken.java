package com._7aske.grain.data.dsl.token;

import com._7aske.grain.data.dsl.Operation;

public class OperationToken extends Token {
    private Token left;
    private Operation operation;
    private Token right;

    public OperationToken(Token left, Operation operation, Token right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public Token getLeft() {
        return left;
    }

    public Operation getOperation() {
        return operation;
    }

    public Token getRight() {
        return right;
    }

    public void setLeft(Token left) {
        this.left = left;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setRight(Token right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "[%s %s %s]".formatted(left, operation, right);
    }

}
