package com._7aske.grain.data.dsl.token;

import com._7aske.grain.data.dsl.Operation;

public abstract class BinaryToken extends Token {
    protected Token left;
    protected Operation operation;
    protected Token right;

    protected BinaryToken(Token left, Operation operation, Token right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public Token getLeft() {
        return left;
    }

    public void setLeft(Token left) {
        this.left = left;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Token getRight() {
        return right;
    }

    public void setRight(Token right) {
        this.right = right;
    }
}
