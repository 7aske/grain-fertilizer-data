package com._7aske.grain.data.factory;

import com._7aske.grain.data.annotation.Param;
import com._7aske.grain.data.annotation.Query;
import com._7aske.grain.data.repository.CrudRepository;
import com._7aske.grain.data.session.TestSessionProvider;
import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryQueryAnnotationResolverInterceptorTest {

    Configuration configuration;
    GrainDataRepositoryFactory factory;

    @BeforeEach
    void setup() {
        configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(TestUserEntity.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        factory = new GrainDataRepositoryFactory(new TestSessionProvider(sessionFactory));
    }

    @AfterEach
    void tearDown() {

    }

    public interface TestUserRepository extends CrudRepository<TestUserEntity, Long> {
        @Query("SELECT u FROM TestUserEntity u WHERE u.name like ?1")
        List<TestUserEntity> findAllByNameLike(String name);

        @Query(value = "SELECT * FROM test_user u WHERE u.name like :name", nativeQuery = true)
        List<TestUserEntity> findAllByNameLikeNative(@Param("name") String name);

        @Query(value = "update test_user set name = ?1 where name = ?2", nativeQuery = true, modifying = true)
        void updateName(String newName, String oldName);
    }

    @Test
    void testQueryAnnotationResolver() {
        TestUserRepository repository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(repository);
        TestUserEntity user = new TestUserEntity();
        user.setName("John Doe");
        user.setAge(25);
        user.setBirthDate(LocalDate.of(1996, 1, 1));
        repository.save(user);

        TestUserEntity user2 = new TestUserEntity();
        user2.setName("Jane Doe");
        user2.setAge(25);
        user2.setBirthDate(LocalDate.of(1996, 1, 1));
        repository.save(user2);

        List<TestUserEntity> users = repository.findAllByNameLike("%John%");
        assertEquals(1, users.size());

        List<TestUserEntity> usersNative = repository.findAllByNameLikeNative("%Jane%");
        assertEquals(1, usersNative.size());
    }

    @Test
    void testModifyingQueryAnnotationResolver() {
        TestUserRepository repository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(repository);
        TestUserEntity user = new TestUserEntity();
        user.setName("Jane Doe");
        user.setAge(25);
        user.setBirthDate(LocalDate.of(1996, 1, 1));
        repository.save(user);

        repository.updateName("John Doe", "Jane Doe");
        List<TestUserEntity> users = repository.findAllByNameLike("%John%");
        assertEquals(1, users.size());
    }

    @Entity(name = "TestUserEntity")
    @Table(name = "test_user")
    public static class TestUserEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "age")
        private int age;

        @Column(name = "birth_date")
        private LocalDate birthDate;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public LocalDate getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
        }
    }

}