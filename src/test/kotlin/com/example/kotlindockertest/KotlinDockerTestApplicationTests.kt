package com.example.kotlindockertest

import com.example.kotlindockertest.controller.QueryWrapper
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.TriggerPathTokenizer
import com.example.kotlindockertest.service.FieldSearcher
import com.example.kotlindockertest.service.TriggerDocumentMatcher
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

    @Autowired lateinit var triggerDocumentMatcher: TriggerDocumentMatcher

    @Test
    fun contextLoads() {

        val parser = Parser()
        val json = ObjectMapper().createObjectNode()
        val raw = "{  query}".replace("\n", "")
//        val raw = "{\n  query {\n    book\n  }\n}".replace("\n", "")
        val str = raw
        println(raw)
        val document = parser.parseDocument(str)
        println(document)
        val path = "['query']"
//        val value =
//        val trigger = TriggerDto(123, "", path, 1, Ope )
//        val match = triggerDocumentMatcher.match(document, "['query']")
//        println(match)
//        val field = fieldSearcher.getField(document, "query") ?: error("")
//        val nested = fieldSearcher.getNestedField(field, "book")
//        println(nested)
//        println(document.findField("adam"))
    }

    @Autowired
    lateinit var tokenizer: TriggerPathTokenizer

    @Test
    fun testTokenizer() {
        val path = "['123'][0]['hello'][0]"
        val result = tokenizer.tokenize(path)
        result.forEach {
            println(it)
        }
    }

    @Test
    fun test23() {
        val query = QueryWrapper("321")
        val writeValueAsString = ObjectMapper().writeValueAsString(query)
        println(writeValueAsString)
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
