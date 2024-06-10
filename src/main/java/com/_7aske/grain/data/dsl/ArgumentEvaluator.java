package com._7aske.grain.data.dsl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class ArgumentEvaluator {
    private final Object[] args;
    private final Root<?> root;
    private final CriteriaQuery<?> query;
    private final CriteriaBuilder builder;


    public ArgumentEvaluator(Object[] args, Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.args = args;
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    public Object evaluate(int argIndex) {
        return args[argIndex];
    }

    public Root<?> getRoot() {
        return root;
    }

    public CriteriaQuery<?> getQuery() {
        return query;
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }
}
