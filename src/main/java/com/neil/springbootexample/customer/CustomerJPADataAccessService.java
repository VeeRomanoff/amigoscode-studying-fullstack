package com.neil.springbootexample.customer;

import com.neil.springbootexample.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao{

    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }


    public Customer selectOneCustomer(Integer id) {
       return customerRepository.findCustomerById(id);
    }

    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean exitsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }
}
