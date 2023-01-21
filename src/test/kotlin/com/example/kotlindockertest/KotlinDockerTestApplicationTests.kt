package com.example.kotlindockertest

import com.example.kotlindockertest.test.FieldSearcher
import com.example.kotlindockertest.test.GraphQLRequestParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.language.*
import graphql.parser.Parser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class KotlinDockerTestApplicationTests {

    @Autowired lateinit var fieldSearcher: FieldSearcher

    @Test
    fun contextLoads() {
        val parser = Parser()
        val json = ObjectMapper().createObjectNode()
        val raw = "{\n  query {\n    book\n  }\n}".replace("\n", "")
        val str = raw
        println(raw)
        val document = parser.parseDocument(str)
        println(document)
//        val field = fieldSearcher.getField(document, "Super")
//        println(field)
//        println(document.findField("adam"))
    }


    private fun JsonNode.getGraphqlQuery() =
        this.get("query")
            .toString()
            .replace("\\r\\n", "")
            .trim('"')

    private fun Document.findField(name: String): Field? {
        definitions.forEach { definition ->
            val operationDefinition = definition as OperationDefinition

            return operationDefinition.selectionSet.findField(name) ?: return@forEach
        }

        return null
    }

    private fun SelectionSet.findField(fieldName: String): Field? {
        selections?.forEach { selection ->
            if ((selection as Field).name == fieldName) {
                return selection
            }

            selection.findFieldAmongChildren(fieldName)?.let { return it }
        }

        return null
    }


    private fun Selection<*>.findFieldAmongChildren(fieldName: String): Field? {
        children?.forEach { child ->
            val childSelection = child as SelectionSet

            return childSelection.findField(fieldName) ?: return@forEach
        }

        return null
    }
}
