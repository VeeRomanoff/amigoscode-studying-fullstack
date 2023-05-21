package com.neil.springbootexample;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContainersTest {
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("")

    @Test
    void canStartPostgresDB() {

    }
}
