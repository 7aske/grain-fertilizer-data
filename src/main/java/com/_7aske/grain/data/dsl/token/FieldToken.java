package com._7aske.grain.data.dsl.token;

public class FieldToken extends Token {
    private final String field;

    public FieldToken(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        return field;
    }
}
