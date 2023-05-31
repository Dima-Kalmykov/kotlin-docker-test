package com.example.kotlindockertest.test

import com.fasterxml.jackson.databind.JsonNode
import graphql.parser.Parser
import org.assertj.core.api.Assertions
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

//@RestController
class RecursiveController(
    private val queryParser: GraphQLRequestParser,
) {

    @PostMapping("/compare")
    fun compareRecursive(@RequestBody query: JsonNode) {
        val parser = Parser()

        println("Got request ${query.toPrettyString()}")
        val parsedUserQuery = queryParser.parseRequest(query)
        println(parsedUserQuery)

        val userDocument = parser.parseDocument(parsedUserQuery)

        println("User document = $userDocument")

        val existingMockQuery = getExistingQuery()
        val document = parser.parseDocument(existingMockQuery)
        val result = Assertions.assertThat(userDocument)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringFieldsMatchingRegexes(". arguments")
            .isEqualTo(document)

        println(result)
        println("Existing document = $document")
    }

    private fun getExistingQuery(): String {
        return """
            query {
                book(id: 123) {
                    name
                }
            }
        """.trimIndent()
    }
}