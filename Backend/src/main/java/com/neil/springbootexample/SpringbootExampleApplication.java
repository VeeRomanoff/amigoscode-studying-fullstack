package com.neil.springbootexample;

import com.github.javafaker.Faker;
import com.neil.springbootexample.customer.Customer;
import com.neil.springbootexample.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;


@SpringBootApplication
public class SpringbootExampleApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootExampleApplication.class, args);
    }

    record Foo(String name) {
    };

    @Bean
    public Foo getFoo() {
        return new Foo("bar");
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            String name = faker.name().firstName();
            var emailAdress = faker.internet().emailAddress();
            Integer age = faker.number().numberBetween(15, 70);
            Customer customer = new Customer(
                    name,
                    emailAdress,
                    age
            );

            customerRepository.save(customer);
        };
    }
}
