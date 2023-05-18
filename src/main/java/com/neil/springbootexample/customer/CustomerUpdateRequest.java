package com.neil.springbootexample.customer;

public record CustomerUpdateRequest(
        String name,
        Integer age,
        String email
) {
}
