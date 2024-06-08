package com._7aske.grain.data.factory;


import com._7aske.grain.core.component.Injectable;
import com._7aske.grain.core.component.Order;
import com._7aske.grain.core.reflect.ProxyInterceptorWrapper;
import com._7aske.grain.core.reflect.factory.GrainFactory;
import com._7aske.grain.data.repository.AbstractCrudRepository;
import com._7aske.grain.data.repository.CrudRepository;
import com._7aske.grain.data.repository.Repository;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class GrainDataRepositoryFactory implements GrainFactory {
    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    private final SessionFactory sessionFactory;
    private final Map<Class<?>, Object> implementationCache;

    public GrainDataRepositoryFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.implementationCache = new HashMap<>();
    }


    Object getImplementation(Class<?> clazz) {
        if (implementationCache.containsKey(clazz)) {
            return implementationCache.get(clazz);
        }

        DynamicType.Builder<?> byteBuddy = new ByteBuddy()
                .subclass(AbstractCrudRepository.class)
                .implement(clazz);

        List<RepositoryAbstractMethodResolverInterceptor> interceptors = new ArrayList<>(getClass().getDeclaredMethods().length);

        for (Method method : clazz.getDeclaredMethods()) {
            RepositoryAbstractMethodResolverInterceptor interceptor =
                    new RepositoryAbstractMethodResolverInterceptor(sessionFactory);

            byteBuddy = byteBuddy.define(method)
                    .intercept(MethodDelegation.to(ProxyInterceptorWrapper.wrap(interceptor)))
                    .annotateMethod(method.getDeclaredAnnotations());

            interceptors.add(interceptor);
        }

        try (DynamicType.Unloaded<?> unloaded = byteBuddy.make()) {
            Optional<TypeDescription.Generic> first = unloaded.getTypeDescription().getInterfaces()
                    .stream()
                    .flatMap(type -> type.getInterfaces().stream())
                    .filter(type -> type.asErasure().isAssignableFrom(CrudRepository.class))
                    .findFirst();

            if (first.isEmpty()) {
                throw new RuntimeException("CrudRepository not implemented");
            }

            TypeList.Generic typeVariables = first.get().getTypeArguments();
            String entityClassFQCN = typeVariables.get(0).asErasure().getName();

            Class<?> entityClass = getClass().getClassLoader().loadClass(entityClassFQCN);

            interceptors.forEach(interceptor -> interceptor.setEntityClass(entityClass));

            Object abstractCrudRepository = unloaded
                    .load(CLASS_LOADER, ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded()
                    .getConstructor(SessionFactory.class, Class.class)
                    .newInstance(sessionFactory, entityClass);

            implementationCache.put(clazz, abstractCrudRepository);

            return abstractCrudRepository;
        } catch (ClassNotFoundException | InvocationTargetException |
                 InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        return Order.HIGHEST_PRECEDENCE + 99;
    }

    @Override
    public boolean supports(Injectable dependency) {
        return Repository.class.isAssignableFrom(dependency.getType());
    }

    @Override
    public <T> T create(Injectable dependency, Object[] args) {
        return (T) getImplementation(dependency.getType());
    }
}
