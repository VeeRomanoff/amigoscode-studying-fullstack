package com.neil.springbootexample;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestContainersTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("neil-dao-unit-test")
                    .withUsername("neil")
                    .withPassword("110011");

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
//        assertThat(postgreSQLContainer.isHealthy()).isTrue();
    }
}
