package com._7aske.grain.data.dsl.token;

public class FieldToken extends Token {
    private String field;

    public FieldToken(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        return "FieldToken{" +
                "field='" + field + '\'' +
                '}';
    }
}
