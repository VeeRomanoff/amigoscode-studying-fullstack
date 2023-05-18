package com.neil.springbootexample.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();

    Customer selectOneCustomer(Integer id);

    void insertCustomer(Customer customer);

    boolean exitsPersonWithEmail(String email);

    void deleteCustomer(Integer id);

    void updateCustomer(Customer customer);
}
