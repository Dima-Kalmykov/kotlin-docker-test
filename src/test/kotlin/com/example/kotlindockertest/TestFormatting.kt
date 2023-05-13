package com.example.kotlindockertest

import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class TestFormatting {

    private fun getUserQuery(): String {
        return """
            query {
                book {
                    name
                }
            }
        """.trimIndent()
    }

    @Test
    fun testUserQuery() {
        println(ZonedDateTime.now().toString())
        val query = getUserQuery()
        println(query.replace("\n", "\\n"))
    }
}