package com.pokemon.microservices.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConfigServerApplicationTest {

    @Test
    void contextLoads() {
        // Test que la aplicación se inicia correctamente
    }
}