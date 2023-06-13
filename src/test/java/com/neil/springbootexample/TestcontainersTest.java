package com.neil.springbootexample;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestcontainersTest extends AbstractTestcontainers {

    @Test
    void checkIfPostgresDataBaseStarts() {
        Assertions.assertThat(postgreSQLContainer.isRunning()).isTrue();
        Assertions.assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}

