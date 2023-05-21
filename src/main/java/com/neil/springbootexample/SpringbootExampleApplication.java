package com.neil.springbootexample;

import com.neil.springbootexample.customer.Customer;
import com.neil.springbootexample.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
public class SpringbootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootExampleApplication.class, args);
	}

	record Foo(String name){};

	@Bean
	public Foo getFoo() {
		return new Foo("bar");
	}
}
