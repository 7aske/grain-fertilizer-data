package com._7aske.grain.data.dsl.token;

public record FieldToken(String field) implements Token {
    @Override
    public String toString() {
        return "FieldToken{" +
               "field='" + field + '\'' +
               '}';
    }
}
