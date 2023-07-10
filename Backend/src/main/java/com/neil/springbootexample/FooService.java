package com.neil.springbootexample;


import org.springframework.stereotype.Service;

@Service
public class FooService {

    private final SpringbootExampleApplication.Foo foo;

    public FooService(SpringbootExampleApplication.Foo foo) {

        this.foo = foo;
    }

    String getFooName() {
        return foo.name();
    }
}
