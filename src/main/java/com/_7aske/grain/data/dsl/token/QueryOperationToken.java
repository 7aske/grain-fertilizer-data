package com._7aske.grain.data.dsl.token;

import com._7aske.grain.data.dsl.QueryOperation;

public class QueryOperationToken extends Token {
    private Token left;
    private QueryOperation queryOperation;
    private Token right;

    public QueryOperationToken(Token left, QueryOperation queryOperation, Token right) {
        this.left = left;
        this.queryOperation = queryOperation;
        this.right = right;
    }

    public Token getLeft() {
        return left;
    }

    public QueryOperation getQueryOperation() {
        return queryOperation;
    }

    public Token getRight() {
        return right;
    }

    public void setLeft(Token left) {
        this.left = left;
    }

    public void setQueryOperation(QueryOperation queryOperation) {
        this.queryOperation = queryOperation;
    }

    public void setRight(Token right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "[%s %s %s]".formatted(left, queryOperation, right);
    }
}
