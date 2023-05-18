package com.neil.springbootexample.customer;

import com.neil.springbootexample.exception.DuplicateResourceException;
import com.neil.springbootexample.exception.RequestValidationException;
import com.neil.springbootexample.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getOneCustomer(Integer id) {
        Customer customer = customerDao.selectOneCustomer(id);

        if (customer == null) {
            throw new ResourceNotFoundException("There's no such customer with id [%s]".formatted(id));
        }
        return customer;
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        // check if email exists
        String email = customerRegistrationRequest.email();
        if (customerDao.exitsPersonWithEmail(email)) {
            throw new DuplicateResourceException("email already taken");
        }

        // add
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age())
        );
     }

     public void deleteCustomer(Integer id) {
        customerDao.deleteCustomer(id);
     }

     public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = getOneCustomer(id);

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.exitsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no changes were made");
        }

        customerDao.updateCustomer(customer);
     }
}
