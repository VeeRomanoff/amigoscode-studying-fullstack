package com.neil.springbootexample.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findCustomerById(Integer id);
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);
}
