package com._7aske.grain.data.dsl;

import com._7aske.grain.data.dsl.ast.Node;
import com._7aske.grain.data.dsl.token.Token;

import java.util.Deque;
import java.util.List;

public class LexingResult {
    private RootOperation rootOperation;
    private Deque<Token> tokens;

    public LexingResult() {
    }

    public RootOperation getRootOperation() {
        return rootOperation;
    }

    public void setRootOperation(RootOperation rootOperation) {
        this.rootOperation = rootOperation;
    }

    public Deque<Token> getTokens() {
        return tokens;
    }

    public void setTokens(Deque<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "LexingResult{" +
               "rootOperation=" + rootOperation +
               ", tokens=" + tokens +
               '}';
    }
}
