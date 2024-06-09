package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.Node;

public class ParsingResult {
    private RootOperation rootOperation;
    private Node tree;

    public ParsingResult() {
    }

    public ParsingResult(RootOperation rootOperation, Node tree) {
        this.rootOperation = rootOperation;
        this.tree = tree;
    }

    public RootOperation getRootOperation() {
        return rootOperation;
    }

    public void setRootOperation(RootOperation rootOperation) {
        this.rootOperation = rootOperation;
    }

    public Node getTree() {
        return tree;
    }

    public void setTree(Node tree) {
        this.tree = tree;
    }

    @Override
    public String toString() {
        return "ParsingResult{" +
                "rootOperation=" + rootOperation +
                ", tree=" + tree +
                '}';
    }
}
