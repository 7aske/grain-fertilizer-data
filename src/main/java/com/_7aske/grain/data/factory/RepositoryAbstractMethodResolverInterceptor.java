package com._7aske.grain.data.factory;

import com._7aske.grain.core.reflect.ProxyInterceptor;
import com._7aske.grain.data.dsl.ArgumentEvaluator;
import com._7aske.grain.data.dsl.DslParser;
import com._7aske.grain.data.dsl.ParsingResult;
import com._7aske.grain.data.dsl.Specification;
import com._7aske.grain.data.dsl.ast.Node;
import com._7aske.grain.data.meta.EntityInformation;
import com._7aske.grain.data.meta.hibernate.HibernateEntityTypeConverter;
import com._7aske.grain.web.page.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.bytebuddy.implementation.bind.annotation.*;
import org.hibernate.SessionFactory;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryAbstractMethodResolverInterceptor implements ProxyInterceptor {
    private final SessionFactory sessionFactory;
    private Class<?> entityClass = null;
    private Node queryAst;

    public RepositoryAbstractMethodResolverInterceptor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @RuntimeType
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object intercept(@This Object self,
                            @Origin Method method,
                            @AllArguments Object[] args,
                            @SuperMethod Method superMethod) throws Throwable {
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root root = criteriaQuery.from(entityClass);

            ArgumentEvaluator evaluator = new ArgumentEvaluator(args, root, criteriaQuery, criteriaBuilder);

            Expression<?> where = getQueryAst(entityManager, method)
                    .toExpression(evaluator);

            Optional<Pageable> pageable = Stream.of(args)
                    .filter(Pageable.class::isInstance)
                    .map(Pageable.class::cast)
                    .findFirst();

            Class<?> returnType = method.getReturnType();
            if (Iterable.class.isAssignableFrom(returnType)) {
                return entityManager.createQuery(criteriaQuery.select(root).where(where))
                        .setFirstResult(pageable.map(Pageable::getPageOffset).orElse(0))
                        .setMaxResults(pageable.map(Pageable::getPageSize).orElse(Integer.MAX_VALUE))
                        .getResultList();
            } else if (Optional.class.isAssignableFrom(returnType)) {
                return Optional.ofNullable(entityManager.createQuery(criteriaQuery.select(root).where(where))
                        .getSingleResult());
            } else if (returnType.isPrimitive()) {
                return entityManager.createQuery(criteriaQuery.select(root).where(where))
                        .getSingleResult();
            } else {
                return entityManager.createQuery(criteriaQuery.select(root).where(where))
                        .getSingleResult();
            }
        }
    }

    private Node getQueryAst(EntityManager entityManager, Method method) {
        if (queryAst != null) {
            return queryAst;
        }

        EntityInformation entityInformation = new HibernateEntityTypeConverter().convert(entityManager, entityClass);
        DslParser parser = new DslParser(entityInformation);
        String methodName = method.getName();

        ParsingResult tokens = parser.parse(methodName);
        queryAst = tokens.getTree();

        return queryAst;
    }
}
