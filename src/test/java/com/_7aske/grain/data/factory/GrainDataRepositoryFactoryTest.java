package com._7aske.grain.data.factory;

import com._7aske.grain.data.repository.CrudRepository;
import com._7aske.grain.data.session.TestSessionProvider;
import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GrainDataRepositoryFactoryTest {

    public interface TestAddressRepository extends CrudRepository<TestAddressEntity, Long> {
    }

    public interface TestCityRepository extends CrudRepository<TestCityEntity, Long> {
    }

    public interface TestUserRepository extends CrudRepository<TestUserEntity, Long> {
        List<TestUserEntity> findAllByName(String name);
        List<TestUserEntity> findAllByNameStartsWith(String name);
        List<TestUserEntity> findAllByAgeLessEqualThan(int age);
        List<TestUserEntity> findAllByBirthDateGreaterThan(LocalDate birthDate);
        List<TestUserEntity> findAllByAddressCityName(String city);
    }

    Configuration configuration;
    GrainDataRepositoryFactory factory;

    @BeforeEach
    void setup() {
        configuration = new Configuration();
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:test");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.addAnnotatedClass(TestUserEntity.class);
        configuration.addAnnotatedClass(TestAddressEntity.class);
        configuration.addAnnotatedClass(TestCityEntity.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        factory = new GrainDataRepositoryFactory(new TestSessionProvider(sessionFactory));
    }

    @Test
    void test() {
        TestUserRepository testUserRepository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(testUserRepository);

        TestUserEntity entity = new TestUserEntity();
        testUserRepository.save(entity);

        TestUserEntity found = testUserRepository.findById(entity.getId());
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testFindByName() {
        TestUserRepository testUserRepository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(testUserRepository);

        TestUserEntity entity = new TestUserEntity();
        entity.setName("test");
        testUserRepository.save(entity);

        List<TestUserEntity> found = testUserRepository.findAllByName("test");

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getName());
    }

    @Test
    void testFindByLike() {
        TestUserRepository testUserRepository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(testUserRepository);

        TestUserEntity entity = new TestUserEntity();
        entity.setName("test");
        testUserRepository.save(entity);

        List<TestUserEntity> found = testUserRepository.findAllByNameStartsWith("te");

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getName());
    }

    @Test
    void testAge() {
        TestUserRepository testUserRepository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(testUserRepository);

        TestUserEntity entity = new TestUserEntity();
        entity.setName("test");
        entity.setAge(20);
        testUserRepository.save(entity);

        List<TestUserEntity> found = testUserRepository.findAllByAgeLessEqualThan(20);

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getName());
        assertEquals(20, found.get(0).getAge());
    }

    @Test
    void testFindAll() {
        TestUserRepository testUserRepository = factory.getImplementation(TestUserRepository.class);
        assertNotNull(testUserRepository);

        TestUserEntity entity = new TestUserEntity();
        entity.setName("test");
        entity.setBirthDate(LocalDate.of(2000, 1, 1));
        testUserRepository.save(entity);

        List<TestUserEntity> found = testUserRepository.findAllByBirthDateGreaterThan(LocalDate.of(1999, 12, 31));

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getName());
    }

    @Test
    void testFindByAddressCity() {
        TestUserRepository testUserRepository = factory.getImplementation(TestUserRepository.class);
        TestAddressRepository addressRepository = factory.getImplementation(TestAddressRepository.class);
        TestCityRepository cityRepository = factory.getImplementation(TestCityRepository.class);
        assertNotNull(testUserRepository);

        TestCityEntity city = new TestCityEntity();
        city.setName("city");
        cityRepository.save(city);

        TestAddressEntity address = new TestAddressEntity();
        address.setCity(city);
        addressRepository.save(address);

        TestUserEntity entity = new TestUserEntity();
        entity.setName("test");
        entity.setAddress(address);
        testUserRepository.save(entity);

        List<TestUserEntity> found = testUserRepository.findAllByAddressCityName("city");

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("test", found.get(0).getName());
    }

    @Entity
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

        @ManyToOne
        private TestAddressEntity address;

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

        public TestAddressEntity getAddress() {
            return address;
        }

        public void setAddress(TestAddressEntity address) {
            this.address = address;
        }
    }

    @Entity
    public static class TestAddressEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private TestCityEntity city;

        @Column(name = "street")
        private String street;

        @Column(name = "zip_code")
        private String zipCode;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public TestCityEntity getCity() {
            return city;
        }

        public void setCity(TestCityEntity city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }

    @Entity
    public static class TestCityEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        @OneToMany
        private List<TestAddressEntity> addresses;

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

        public List<TestAddressEntity> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<TestAddressEntity> addresses) {
            this.addresses = addresses;
        }
    }
}