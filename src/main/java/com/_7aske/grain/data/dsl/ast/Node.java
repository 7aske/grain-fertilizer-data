package com._7aske.grain.data.dsl.ast;

import com._7aske.grain.data.dsl.ArgumentEvaluator;
import jakarta.persistence.criteria.*;

public interface Node {
    <T> Expression<T> toExpression(ArgumentEvaluator argumentEvaluator);
}
