package com._7aske.grain.data.factory;

import com._7aske.grain.core.reflect.ProxyInterceptor;
import com._7aske.grain.data.dsl.DslParser;
import com._7aske.grain.data.dsl.ParsingResult;
import com._7aske.grain.data.dsl.ast.Node;
import com._7aske.grain.data.meta.EntityInformation;
import com._7aske.grain.data.meta.hibernate.HibernateEntityTypeConvterer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.bytebuddy.implementation.bind.annotation.*;
import org.hibernate.SessionFactory;

import java.lang.reflect.Method;

public class RepositoryAbstractMethodResolverInterceptor implements ProxyInterceptor {
    private final SessionFactory sessionFactory;
    private Class<?> entityClass = null;

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
            EntityInformation entityInformation = new HibernateEntityTypeConvterer().convert(entityManager
                    .getMetamodel().entity(entityClass));
            DslParser parser = new DslParser(entityInformation, args);
            String methodName = method.getName();

            ParsingResult tokens = parser.parse(methodName);
            Node start = tokens.getTree();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(entityClass);
            Root root = criteriaQuery.from(entityClass);

            Expression<Object> where = start.toPredicate(root, criteriaQuery, criteriaBuilder);
            return entityManager.createQuery(criteriaQuery.select(root).where(where))
                    .getResultList();
        }
    }
}
