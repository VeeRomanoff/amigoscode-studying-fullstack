package com.neil.springbootexample.controller;

import com.neil.springbootexample.customer.Customer;
import com.neil.springbootexample.customer.CustomerRegistrationRequest;
import com.neil.springbootexample.customer.CustomerService;
import com.neil.springbootexample.customer.CustomerUpdateRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomersController {

    private final CustomerService customerService;

    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable("id") Integer id) {
        return customerService.getOneCustomer(id);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        customerService.addCustomer(customerRegistrationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("/{id}")
    public void updateCustomer(
            @PathVariable("id") Integer id,
            @RequestBody CustomerUpdateRequest updatedCustomer) {

        customerService.updateCustomer(id, updatedCustomer);
    }
}
