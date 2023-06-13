package com.neil.springbootexample.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();

    Customer selectOneCustomer(int id);

    void insertCustomer(Customer customer);

    boolean exitsPersonWithEmail(String email);

    void deleteCustomer(Integer id);

    void updateCustomer(Customer customer);
    boolean existCustomerWithId(Integer id);
}
