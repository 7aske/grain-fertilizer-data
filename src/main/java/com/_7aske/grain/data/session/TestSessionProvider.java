package com._7aske.grain.data.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TestSessionProvider implements SessionProvider {
    private final SessionFactory sessionFactory;

    public TestSessionProvider(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getSession() {
        return sessionFactory.openSession();
    }
}
