package com._7aske.grain.data;

import com._7aske.grain.core.component.Grain;
import com._7aske.grain.core.configuration.GrainFertilizer;
import com._7aske.grain.data.factory.GrainDataRepositoryFactory;
import com._7aske.grain.data.factory.GrainDataRepositoryInterceptor;
import org.hibernate.SessionFactory;

@GrainFertilizer
public class GrainDataFertilizer {
    @Grain
    public GrainDataRepositoryFactory grainDataRepositoryInterceptor(SessionFactory sessionFactory) {
        return new GrainDataRepositoryFactory(sessionFactory);
    }
}
