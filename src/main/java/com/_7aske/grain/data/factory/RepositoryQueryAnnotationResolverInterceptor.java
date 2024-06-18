package com._7aske.grain.data.factory;

import com._7aske.grain.data.annotation.Param;
import com._7aske.grain.data.dsl.Specification;
import com._7aske.grain.data.session.SessionProvider;
import com._7aske.grain.web.page.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryQueryAnnotationResolverInterceptor implements RepositoryProxyInterceptor {
    private final SessionProvider sessionProvider;
    private Class<?> entityClass = null;

    public RepositoryQueryAnnotationResolverInterceptor(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @RuntimeType
    public Object intercept(@This Object self,
                            @Origin Method method,
                            @AllArguments Object[] args,
                            @SuperMethod Method superMethod) throws Throwable {
        EntityManager entityManager = sessionProvider.getSession();

        com._7aske.grain.data.annotation.Query queryAnnotation =
                method.getAnnotation(com._7aske.grain.data.annotation.Query.class);
        Class<?> returnType = method.getReturnType();

        Query query = getQuery(entityManager, queryAnnotation.value(), method.getParameters(), args, queryAnnotation.nativeQuery());

        Optional<Pageable> pageable = Stream.of(args)
                .filter(Pageable.class::isInstance)
                .map(Pageable.class::cast)
                .findFirst();


        if (queryAnnotation.modifying()) {
            entityManager.getTransaction().begin();
            int results = query.executeUpdate();
            entityManager.getTransaction().commit();

            if (returnType.equals(Void.TYPE)) {
                return null;
            } else if (returnType.isAssignableFrom(Number.class)) {
                return results;
            } else {
                throw new RuntimeException("Modifying query must return void or Number");
            }
        }

        if (Iterable.class.isAssignableFrom(returnType)) {
            if (pageable.isPresent()) {
                query.setFirstResult(pageable.get().getPageOffset());
                query.setMaxResults(pageable.get().getPageSize());
            }

            return query.getResultList();
        } else if (Stream.class.isAssignableFrom(returnType)) {
            return query.getResultStream();
        } else if (Optional.class.isAssignableFrom(returnType)) {
            return Optional.ofNullable(query.getSingleResult());
        } else {
            return query.getSingleResult();
        }
    }

    private Query getQuery(EntityManager entityManager, String queryString, Parameter[] params, Object[] args, boolean isNative) {
        Query query;
        if (isNative) {
            query = entityManager.createNativeQuery(queryString, entityClass);
        } else {
            query = entityManager.createQuery(queryString, entityClass);
        }

        int paramCount = 1;
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(Param.class)) {
                query.setParameter(params[i].getAnnotation(Param.class).value(), args[i]);
            } else if (!(args[i] instanceof Pageable ||
                       args[i] instanceof Specification<?>)) {
                query.setParameter(paramCount++, args[i]);
            }

        }

        return query;
    }
}
