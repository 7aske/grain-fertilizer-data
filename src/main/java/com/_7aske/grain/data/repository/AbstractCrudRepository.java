package com._7aske.grain.data.repository;

import com._7aske.grain.data.dsl.Specification;
import com._7aske.grain.data.session.SessionProvider;
import com._7aske.grain.web.page.Pageable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {
    private final SessionProvider sessionProvider;
    private final Class<T> clazz;

    protected AbstractCrudRepository(SessionProvider sessionProvider, Class<T> clazz) {
        this.sessionProvider = sessionProvider;
        this.clazz = clazz;
    }

    @Override
    public List<T> findAll(Specification<T> specification, Pageable pageable) {
        Session session = sessionProvider.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> root = query.from(clazz);

        return session.createQuery(query.select(root)
                        .where(specification.toPredicate(root, query, cb)))
                .setFirstResult(pageable.getPageOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        Session session = sessionProvider.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        Root<T> root = query.from(clazz);

        return session.createQuery(query.select(root)
                        .where(specification.toPredicate(root, query, cb)))
                .getResultList();
    }

    @Override
    public T findById(ID id) {
        Session session = sessionProvider.getSession();
        return session.find(clazz, id);
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        Session session = sessionProvider.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);
        CriteriaQuery<T> select = query.select(query.from(clazz));
        return session.createQuery(select).setFirstResult(pageable.getPageOffset()).setMaxResults(pageable.getPageSize()).getResultList();
    }

    @Override
    public List<T> findAll() {
        Session session = sessionProvider.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(clazz);

        return session.createQuery(query.select(query.from(clazz)))
                .getResultList();
    }

    @Override
    public T save(T entity) {
        Session session = sessionProvider.getSession();
        try {
            session.getTransaction().begin();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public T update(T entity) {
        Session session = sessionProvider.getSession();
        try {
            session.getTransaction().begin();
            entity = session.merge(entity);
            session.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void deleteById(ID id) {
        Session session = sessionProvider.getSession();
        try {
            session.getMetamodel().entity(clazz);
            T entity = session.find(clazz, id);
            if (entity == null) {
                return;
            }
            session.getTransaction().begin();
            session.remove(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(T entity) {
        Session session = sessionProvider.getSession();
        try {
            session.getTransaction().begin();
            session.remove(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public long count() {
        Session session = sessionProvider.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        query.select(cb.count(query.from(clazz)));
        return session.createQuery(query).getSingleResult();
    }

}
