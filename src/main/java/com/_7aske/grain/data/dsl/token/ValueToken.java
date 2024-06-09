package com._7aske.grain.data.dsl.token;

public class ValueToken {
    private Object value;

    public ValueToken(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ValueToken{" +
                "value=" + value +
                '}';
    }
}
