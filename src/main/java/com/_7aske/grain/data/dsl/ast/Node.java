package com._7aske.grain.data.dsl.ast;

import jakarta.persistence.criteria.*;

public abstract class Node {
    public abstract <T> Expression<T> toPredicate(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder builder);
}
