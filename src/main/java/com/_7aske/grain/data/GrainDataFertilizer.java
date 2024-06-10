package com._7aske.grain.data;

import com._7aske.grain.core.component.Grain;
import com._7aske.grain.core.component.Order;
import com._7aske.grain.core.configuration.GrainFertilizer;
import com._7aske.grain.data.factory.GrainDataRepositoryFactory;
import com._7aske.grain.data.session.HibernateSessionProvider;
import com._7aske.grain.data.session.OpenSessionInViewMiddleware;
import com._7aske.grain.data.session.SessionProvider;
import com._7aske.grain.web.requesthandler.middleware.Middleware;
import org.hibernate.SessionFactory;

@GrainFertilizer
public class GrainDataFertilizer {
    @Grain
    @Order
    public GrainDataRepositoryFactory grainDataRepositoryInterceptor(SessionProvider sessionProvider) {
        return new GrainDataRepositoryFactory(sessionProvider);
    }

    @Grain
    public Middleware openSessionInViewMiddleware(SessionFactory sessionFactory) {
        return new OpenSessionInViewMiddleware(sessionFactory);
    }

    @Grain
    @Order(Order.HIGHEST_PRECEDENCE)
    public SessionProvider hibernateSessionProvider(SessionFactory sessionFactory) {
        return new HibernateSessionProvider(sessionFactory);
    }
}
