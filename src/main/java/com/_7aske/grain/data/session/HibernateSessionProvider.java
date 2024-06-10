package com._7aske.grain.data.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateSessionProvider implements SessionProvider {
    private final SessionFactory sessionFactory;

    public HibernateSessionProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getSession() {
        Session session = SessionHolder.getSession();
        if (session == null) {
            session = sessionFactory.openSession();
            SessionHolder.setSession(session);
        }

        return session;
    }
}
