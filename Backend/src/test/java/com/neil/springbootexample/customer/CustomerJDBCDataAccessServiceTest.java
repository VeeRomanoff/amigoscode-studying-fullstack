package com.neil.springbootexample.customer;

import com.neil.springbootexample.AbstractTestcontainers;
import com.neil.springbootexample.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate()
        );
    }

    @Test
    void selectAllCustomers() {

        // GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                new Random().nextInt(10, 50)
        );

        underTest.insertCustomer(customer);

        // WHEN
        List<Customer> customers = underTest.selectAllCustomers();

        // THEN
        Assertions.assertThat(customers).isNotEmpty();
    }

    @Test
    void selectOneCustomer() {
        // GIVEN

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(10, 50)
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(cus -> cus.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        Customer actual = underTest.selectOneCustomer(id);

        // THEN
        Assertions.assertThat(actual).isNotNull();
    }

    @Test
    void willReturnEmptyWhenSelectOneCustomer() {
        // GIVEN
        int id = -1;

        // WHEN
        Throwable thrown = catchThrowable(() -> underTest.selectOneCustomer(id));

        // THEN
        Assertions.assertThat(thrown).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id %s not found".formatted(id));
    }

    @Test
    void insertCustomer() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(10, 50)
        );

        // WHEN
        underTest.insertCustomer(customer);

        // THEN
        Assertions.assertThat(underTest.exitsPersonWithEmail(customer.getEmail())).isTrue();
    }

    @Test
    void exitsPersonWithEmail() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(10, 50)
        );

        underTest.insertCustomer(customer);

        // WHEN
        var actual = underTest.exitsPersonWithEmail(email);

        // THEN
        Assertions.assertThat(actual).isEqualTo(true);
    }

    @Test
    void exitsPersonWithEmailReturnsFalseWhenDoesNotExist() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // WHEN
        boolean actual = underTest.exitsPersonWithEmail(email);

        //THEN
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomer() {
        // GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                new Random().nextInt(10, 50)
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(cus -> cus.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // WHEN
        underTest.deleteCustomer(id);

        // THEN

        Throwable thrown = catchThrowable(() -> underTest.selectOneCustomer(id));
        Assertions.assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void testUpdateCustomer() {
        // create a new customer
        Customer customer = new Customer(1, "William", "John Doe", 30);
        underTest.insertCustomer(customer);

        // update the customer
        underTest.updateCustomer(customer);

        // retrieve the updated customer from the database
        Customer updatedCustomer = underTest.selectOneCustomer(1);

        // assert that the customer has been updated correctly
        Assertions.assertThat(updatedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(updatedCustomer.getAge()).isEqualTo(customer.getAge());
    }
}