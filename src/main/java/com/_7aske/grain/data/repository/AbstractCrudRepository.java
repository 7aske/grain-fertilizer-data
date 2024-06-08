package com._7aske.grain.data.repository;

import com._7aske.grain.web.page.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.SessionFactory;

import java.util.List;

public class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {
    private final SessionFactory sessionFactory;
    private final Class<T> clazz;

    protected AbstractCrudRepository(SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    @Override
    public T findById(ID id) {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            return entityManager.find(clazz, id);
        }
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(clazz);
            CriteriaQuery<T> select = query.select(query.from(clazz));
            return entityManager.createQuery(select)
                    .setFirstResult(pageable.getPageOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    @Override
    public List<T> findAll() {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(clazz);
            CriteriaQuery<T> select = query.select(query.from(clazz));
            return entityManager.createQuery(select).getResultList();
        }
    }

    @Override
    public T save(T entity) {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            return entity;
        }
    }

    @Override
    public T update(T entity) {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entity = entityManager.merge(entity);
            entityManager.getTransaction().commit();
            return entity;
        }
    }

    @Override
    public void delete(T entity) {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }
    }

}
