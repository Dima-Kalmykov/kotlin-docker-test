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

    fun getGraphqlSchema(): String {
        return """
type Publishing {
    id: ID!
    year: String
}

type Book {
    publishing(id: Int): Publishing
    title: String
    pages: Int
}

type Query {
    book: Book
}
        """.trimIndent()
    }

    @Test
    fun pr() {
        println(getGraphqlSchema().replace("\n", "\\n"))
    }



    @Test
    fun testUserQuery() {
        val query = """
            query {
                book {
                    publishing(id: 13) {
                        year
                    }
                    title
                    pages
                }
            }
        """.trimIndent()

        println(query.replace("\n", "\\n"))
        println()
    }

    @Test
    fun testUserQuery2() {
        val query2 = """
            {
                "data": {
                    "publishing": {
                        "id": 13,
                        "year": "2001"
                    },
                    "title": "testBook",
                    "pages": 125
                }
            }
        """.trimIndent()

        println(query2.replace("\n", "\\n").replace("\"", "\\\""))
        println()
    }
}