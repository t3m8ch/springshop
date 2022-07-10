package io.github.t3m8ch.springshop

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class ContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${pgsqlContainer.jdbcUrl}",
            "spring.datasource.username=${pgsqlContainer.username}",
            "spring.datasource.password=${pgsqlContainer.password}",
        ).applyTo(applicationContext.environment)
    }

    companion object {
        private val pgsqlContainer = PostgreSQLContainer(DockerImageName.parse("postgres"))
            .withDatabaseName("springshop-db")
            .withUsername("sa")
            .withPassword("sa")

        init {
            pgsqlContainer.start()
        }
    }
}