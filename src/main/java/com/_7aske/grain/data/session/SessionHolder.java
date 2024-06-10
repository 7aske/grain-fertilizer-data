package com._7aske.grain.data.session;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.Optional;

public class SessionHolder {
    private static final ThreadLocal<Session> session = new ThreadLocal<>();

    private SessionHolder() {
    }

    public static void setSession(Session session) {
        SessionHolder.session.set(session);
    }

    public static Session getSession() {
        return session.get();
    }

    public static void clearSession() {
        try {
            Optional.ofNullable(session.get())
                    .ifPresent(Session::close);
        } catch (HibernateException e) {
            // ignored
        }

        session.remove();
    }
}
