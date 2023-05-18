package com.neil.springbootexample.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {}
