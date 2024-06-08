package com._7aske.grain.data.factory;

import com._7aske.grain.core.reflect.ProxyInterceptor;
import com._7aske.grain.data.repository.AbstractCrudRepository;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class GrainDataRepositoryInterceptor implements ProxyInterceptor {
    private final AbstractCrudRepository<?, ?> implementation;

    public GrainDataRepositoryInterceptor(AbstractCrudRepository<?, ?> implementation) {
        this.implementation = implementation;
    }

    @Override
    public Object intercept(Object self, Method method, Object[] args, Method superMethod) throws Throwable {
        if (Modifier.isAbstract(method.getModifiers())) {
            throw new UnsupportedOperationException("Method is abstract");
        }

        return method.invoke(implementation, args);
    }
}
