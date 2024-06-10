package com._7aske.grain.data.dsl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public interface Specification<T> {
    static <T> Specification<T> not(Specification<T> spec) {

        return spec == null
                ? (root, query, builder) -> null
                : (root, query, builder) -> builder.not(spec.toPredicate(root, query, builder));
    }

    static <T> Specification<T> where(Specification<T> spec) {
        return spec == null ? (root, query, builder) -> null : spec;
    }

    default Specification<T> and(Specification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder::and);
    }

    default Specification<T> or(Specification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder::or);
    }

    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

    static <T> Specification<T> allOf(Iterable<Specification<T>> specifications) {

        return StreamSupport.stream(specifications.spliterator(), false) //
                .reduce(Specification.where(null), Specification::and);
    }

    @SafeVarargs
    static <T> Specification<T> allOf(Specification<T>... specifications) {
        return allOf(Arrays.asList(specifications));
    }

    static <T> Specification<T> anyOf(Iterable<Specification<T>> specifications) {

        return StreamSupport.stream(specifications.spliterator(), false) //
                .reduce(Specification.where(null), Specification::or);
    }

    @SafeVarargs
    static <T> Specification<T> anyOf(Specification<T>... specifications) {
        return anyOf(Arrays.asList(specifications));
    }
}
