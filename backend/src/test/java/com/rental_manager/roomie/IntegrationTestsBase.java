package com.rental_manager.roomie;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class IntegrationTestsBase {

    private static final String initScript = "test_database_init.sql";

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.5")
            .withDatabaseName("test_postgres")
            .withUsername("test_postgres")
            .withPassword("test_postgres")
            .withInitScript(initScript);

    static {
        postgres.start();
    }

    @DynamicPropertySource
    public static void additionalProps(DynamicPropertyRegistry registry) {
        registry.add("datasource.database.url", () -> "jdbc:postgresql://localhost:" + postgres.getFirstMappedPort() + "/rental_system");
    }

}
