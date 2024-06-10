package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.*;

public abstract class Node {
    public abstract <T> Expression<T> toPredicate(ArgumentEvaluator argumentEvaluator);
}
