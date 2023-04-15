package com.example.kotlindockertest

import org.junit.jupiter.api.Test

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
        val query = getUserQuery()
        println(query.replace("\n", "\\n"))
    }
}