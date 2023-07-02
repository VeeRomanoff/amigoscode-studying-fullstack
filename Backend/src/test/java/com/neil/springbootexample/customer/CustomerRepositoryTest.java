package com.neil.springbootexample.customer;

import com.neil.springbootexample.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existCustomerById() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(10, 50)
        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(cus -> cus.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        var actual = underTest.findCustomerById(id);

        // THEN
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    void existCustomerByIdWhenIdNotPresent() {
        // GIVEN
        int id = -1;

        // WHEN
        var actual = underTest.findCustomerById(id);

        // THEN
        Assertions.assertThat(actual).isNull();
    }


    @Test
    void existsCustomerByEmail() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(10, 50)
        );

        underTest.save(customer);

        // WHEN
        var actual = underTest.existsCustomerByEmail(email);

        // THEN
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // WHEN
        var actual = underTest.existsCustomerByEmail(email);

        // THEN
        Assertions.assertThat(actual).isFalse();
    }
}