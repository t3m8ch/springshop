package io.github.t3m8ch.springshop

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(initializers = [ContextInitializer::class])
class LoadContextTest {
    @Test
    fun `test load context`() {
    }
}
