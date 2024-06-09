package com._7aske.grain.data.factory;

import com._7aske.grain.data.repository.AbstractCrudRepository;
import com._7aske.grain.data.repository.CrudRepository;
import jakarta.persistence.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.dynamic.DynamicType;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GrainDataRepositoryFactoryTest {
    @Entity
    public static class TestEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface TestRepository extends CrudRepository<TestEntity, Long> {
        List<TestEntity> findAllByNameNotEquals(String name);
    }

    Configuration configuration;

    @BeforeEach
    void setup() {
        configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
    }

    @Test
    void test() {
        configuration.addAnnotatedClass(TestEntity.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        GrainDataRepositoryFactory factory = new GrainDataRepositoryFactory(sessionFactory);
        AbstractCrudRepository<TestEntity, Long> testRepository = (AbstractCrudRepository<TestEntity, Long>) factory.getImplementation(TestRepository.class);
        assertNotNull(testRepository);

        TestEntity entity = new TestEntity();
        testRepository.save(entity);

        TestEntity found = testRepository.findById(entity.getId());
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    @Disabled
    void testFindByName() {
        configuration.addAnnotatedClass(TestEntity.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        GrainDataRepositoryFactory factory = new GrainDataRepositoryFactory(sessionFactory);
        AbstractCrudRepository<TestEntity, Long> testRepository = (AbstractCrudRepository<TestEntity, Long>) factory.getImplementation(TestRepository.class);
        assertNotNull(testRepository);

        TestEntity entity = new TestEntity();
        entity.setName("test");
        testRepository.save(entity);

        List<TestEntity> found = ((TestRepository) testRepository).findAllByNameNotEquals("test1");

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getName());
    }
}