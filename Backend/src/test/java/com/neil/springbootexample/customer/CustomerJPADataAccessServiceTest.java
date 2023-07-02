package com.neil.springbootexample.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // WHEN
        underTest.selectAllCustomers();

        // THEN
        Mockito.verify(customerRepository)
                .findAll();
    }

    @Test
    void selectOneCustomer() {
        // GIVEN
        int id = 1;

        // WHEN
        underTest.selectOneCustomer(id);

        // THEN
        Mockito.verify(customerRepository)
                .findCustomerById(id);
    }

    @Test
    void insertCustomer() {
        // GIVEN
        Customer customer = new Customer("Name", "jack@email.com", 22);

        // WHEN
        underTest.insertCustomer(customer);

        // THEN
        Mockito.verify(customerRepository)
                .save(customer);
    }

    @Test
    void exitsPersonWithEmail() {
        // GIVEN
        String email = "email@email.com";

        // WHEN
        underTest.exitsPersonWithEmail(email);

        // THEN
        Mockito.verify(customerRepository)
                .existsCustomerByEmail(email);
    }

    @Test
    void deleteCustomer() {
        // GIVEN
        int id = 1;

        // WHEN
        underTest.deleteCustomer(id);

        // THEN
        Mockito.verify(customerRepository)
                .deleteById(id);
    }

    @Test
    void updateCustomer() {
        // GIVEN
        Customer customer = new Customer("Name", "jack@email.com", 22);

        // WHEN
        underTest.updateCustomer(customer);

        // THEN
        Mockito.verify(customerRepository)
                .save(customer);
    }
}