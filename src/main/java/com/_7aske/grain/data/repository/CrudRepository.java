package com._7aske.grain.data.repository;

import com._7aske.grain.web.page.Pageable;

import java.util.List;

public interface CrudRepository<T, ID> extends Repository<T, ID> {
    T findById(ID id);

    List<T> findAll(Pageable pageable);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void deleteById(ID id);

    void delete(T entity);

    long count();
}
