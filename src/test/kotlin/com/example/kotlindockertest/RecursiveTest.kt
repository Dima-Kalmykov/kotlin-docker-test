package com.example.kotlindockertest

import graphql.language.Definition
import graphql.language.Document
import graphql.language.Field
import graphql.language.OperationDefinition
import graphql.language.SelectionSet
import graphql.parser.Parser
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RecursiveTest {

    private fun getExistingQuery(): String {
        return """
            query {
                book(id: 123) {
                    name
                    page
                }
            }
        """.trimIndent()
    }

        private fun getExistingQuery2(): String {
            return """
            query {
                book(id: 456) {
                    name
                }
            }
        """.trimIndent()
        }

    @Test
    fun compare() {
        val parser = Parser()

        val a = getExistingQuery()
        val b = getExistingQuery2()

        val doc1 = parser.parseDocument(a)
        val doc2 = parser.parseDocument(b)
//        System.err.println("Doc1 = $doc1")
        System.err.println("Doc1 = $doc1")
        System.err.println("Doc2 = $doc2")
        System.err.println("-".repeat(50))
        val doc3 = doc1.toString().replace(Regex(", arguments=\\[.*?]"), "")
        val doc4 = doc2.toString().replace(Regex(", arguments=\\[.*?]"), "")
//        doc1.selectionSet
//        System.err.println("Doc2 = $doc2")
        System.err.println("Doc3 = $doc3")
        System.err.println("Doc4 = $doc4")

        assertThat(doc3).isEqualTo(doc4)
//        val result = Assertions.assertThat(doc3)
//            .usingRecursiveComparison()
//            .ignoringCollectionOrder()
//            .isEqualTo(doc4)
    }

    private fun Document.isEqualTo(other: Document) {
        val def1 = this.definitions.first() as OperationDefinition
        val def2 = this.definitions.first() as OperationDefinition


    }
}