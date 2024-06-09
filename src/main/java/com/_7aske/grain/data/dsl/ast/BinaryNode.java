package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.Operation;

public abstract class BinaryNode extends Node {
    protected Node left;
    protected Operation operation;
    protected Node right;

    protected BinaryNode(Node left, Operation operation, Node right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
