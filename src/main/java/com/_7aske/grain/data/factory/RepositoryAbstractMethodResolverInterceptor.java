package com._7aske.grain.data.factory;

import com._7aske.grain.core.reflect.ProxyInterceptor;
import com._7aske.grain.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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


        return null;
    }
}
