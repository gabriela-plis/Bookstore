package app.backend

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class IntegrationTestConfig extends Specification {

    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("bookstore")
            .withUsername("postgres")
            .withPassword("postgres")

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        postgres.start()

        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl())
        registry.add("spring.datasource.username", () -> postgres.getUsername())
        registry.add("spring.datasource.password", () -> postgres.getPassword())
    }
}