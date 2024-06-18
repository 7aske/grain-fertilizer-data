package com._7aske.grain.data.factory;

import java.util.HashMap;
import java.util.Map;

public class RepositoryCache {
    private final Map<Class<?>, Object> implementationCache;

    public RepositoryCache() {
        this.implementationCache = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> repositoryClass) {
        return (T) implementationCache.get(repositoryClass);
    }

    public <T> void put(Class<T> repositoryClass, T repository) {
        implementationCache.put(repositoryClass, repository);
    }

    public boolean contains(Class<?> repositoryClass) {
        return implementationCache.containsKey(repositoryClass);
    }

    public void clear() {
        implementationCache.clear();
    }
}
