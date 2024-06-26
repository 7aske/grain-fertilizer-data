package com._7aske.grain.data.repository;

import com._7aske.grain.data.dsl.Specification;
import com._7aske.grain.web.page.Pageable;

import java.util.List;

/**
 * Base interface for CRUD operations.
 *
 * @param <T>  Entity type
 * @param <ID> Entity ID type
 */
public interface CrudRepository<T, ID> extends Repository<T, ID> {
    List<T> findAll(Specification<T> specification, Pageable pageable);

    List<T> findAll(Specification<T> specification);

    T findById(ID id);

    List<T> findAll(Pageable pageable);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void deleteById(ID id);

    void delete(T entity);

    long count();
}
