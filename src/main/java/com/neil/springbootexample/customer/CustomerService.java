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
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );

        customerDao.insertCustomer(customer);
     }

     public void deleteCustomer(Integer id) {
        if (!customerDao.existCustomerWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }

        customerDao.deleteCustomer(id);
     }

     public void updateCustomer(Integer id, CustomerUpdateRequest updatedRequest) {
        Customer customer = getOneCustomer(id);

        boolean changes = false;

        if (updatedRequest.name() != null && !updatedRequest.name().equals(customer.getName())) {
            customer.setName(updatedRequest.name());
            changes = true;
        }

        if (updatedRequest.age() != null && !updatedRequest.age().equals(customer.getAge())) {
            customer.setAge(updatedRequest.age());
            changes = true;
        }

        if (updatedRequest.email() != null && !updatedRequest.email().equals(customer.getEmail())) {
            if (customerDao.exitsPersonWithEmail(updatedRequest.email())) {
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updatedRequest.email());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no changes were made");
        }

        customerDao.updateCustomer(customer);
     }
}
