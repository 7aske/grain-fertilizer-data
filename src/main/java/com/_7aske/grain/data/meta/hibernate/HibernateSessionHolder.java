package com._7aske.grain.data.meta.hibernate;

import com._7aske.grain.logging.Logger;
import com._7aske.grain.logging.LoggerFactory;
import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

public class HibernateSessionHolder {
    private static SessionFactory sessionFactory = null;
    private static final ThreadLocal<Session> session = ThreadLocal.withInitial(() -> null);
    private static final Logger logger = LoggerFactory.getLogger(HibernateSessionHolder.class);

    private HibernateSessionHolder() {}

    public static void setSessionFactory(SessionFactory sessionFactory) {
        logger.debug("Setting up session factory");
        HibernateSessionHolder.sessionFactory = sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void setSession(Session session) {
        HibernateSessionHolder.session.set(session);
    }


    public static Session getSession() {
        logger.debug("Getting Session");
        if (session.get() != null) {
            return session.get();
        }

        Session s;
        try {
            s = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            s = sessionFactory.openSession();
        }

        session.set(s);

        return s;
    }

    public static void clearSession() {
        HibernateSessionHolder.session.remove();
    }
}
