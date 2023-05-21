package com.neil.springbootexample.customer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;


class CustomerJDBCDataAccessServiceTest {

    private CustomerJDBCDataAccessService underTest;


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                new JdbcTemplate(),
        );
    }

    @Test
    void selectAllCustomers() {
    }

    @Test
    void selectOneCustomer() {
    }

    @Test
    void insertCustomer() {
    }

    @Test
    void exitsPersonWithEmail() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void updateCustomer() {
    }
}