package com._7aske.grain.data.factory;

import com._7aske.grain.core.reflect.ProxyInterceptor;

public interface RepositoryProxyInterceptor extends ProxyInterceptor {
    void setEntityClass(Class<?> entityClass);
}
