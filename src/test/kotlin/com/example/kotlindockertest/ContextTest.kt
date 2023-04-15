package com.example.kotlindockertest

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContextTest {

    @Test
    fun `test tabs`() {
        print(getUserQuery().replace("\n", "\\n"))
    }

    private fun getUserQuery(): String {
        return """
            query {
                book {
                    name
                }
            }
        """.trimIndent()
    }
}
