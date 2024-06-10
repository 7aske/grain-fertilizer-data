package com._7aske.grain.data.repository;

import com._7aske.grain.data.dsl.Specification;
import com._7aske.grain.web.page.Pageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
    public List<T> findAll(Specification<T> specification, Pageable pageable) {
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(clazz);
            Root<T> root = query.from(clazz);
            CriteriaQuery<T> select = query.select(query.from(clazz));

            return entityManager.createQuery(select.where(specification.toPredicate(root, query, cb)))
                    .setFirstResult(pageable.getPageOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        }
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(clazz);
            Root<T> root = query.from(clazz);
            CriteriaQuery<T> select = query.select(query.from(clazz));

            return entityManager.createQuery(select.where(specification.toPredicate(root, query, cb)))
                    .getResultList();
        }
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
    public void deleteById(ID id) {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            entityManager.getMetamodel().entity(clazz);
            T entity = entityManager.find(clazz, id);
            if (entity == null) {
                return;
            }
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
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

    @Override
    public long count() {
        try(EntityManager entityManager = sessionFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            query.select(cb.count(query.from(clazz)));
            return entityManager.createQuery(query).getSingleResult();
        }
    }

}
