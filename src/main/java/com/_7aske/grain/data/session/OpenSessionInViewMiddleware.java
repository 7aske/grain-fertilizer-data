package com._7aske.grain.data.session;

import com._7aske.grain.web.http.HttpRequest;
import com._7aske.grain.web.http.HttpResponse;
import com._7aske.grain.web.requesthandler.middleware.Middleware;
import org.hibernate.SessionFactory;

public class OpenSessionInViewMiddleware implements Middleware {
    private final SessionFactory sessionFactory;

    public OpenSessionInViewMiddleware(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        SessionHolder.setSession(sessionFactory.openSession());
    }

    @Override
    public void afterHandle(HttpRequest request, HttpResponse response) {
        SessionHolder.clearSession();
    }
}