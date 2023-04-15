package com.example.kotlindockertest.test

import com.example.kotlindockertest.model.SchemaDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.FieldSearcher
import com.example.kotlindockertest.service.GraphQLQueryParser
import com.example.kotlindockertest.service.RedirectService
import com.example.kotlindockertest.service.TriggerDocumentMatcher
import com.example.kotlindockertest.service.schema.SchemaValidator
import com.fasterxml.jackson.databind.JsonNode
import graphql.analysis.*
import graphql.language.*
import graphql.parser.Parser
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.swing.text.html.parser.DocumentParser


//@RestController
class TestController(
    private val fieldSearcher: FieldSearcher,
    private val queryParser: GraphQLQueryParser,
    private val triggerDocumentMatcher: TriggerDocumentMatcher,
    private val factory: ConfigurableListableBeanFactory,
    private val schemaValidator: SchemaValidator,
    private val redirectService: RedirectService,
) {

    companion object : Thread()

    data class Parent(val name: String, val age: Int)

    @PostMapping("/test")
    fun test2(@RequestBody body: JsonNode): Int {
        println("Got request with query ${body.toPrettyString()}")
//        val parseRequest = queryParser.parseRequest(body.get("value1"))
//        println(parseRequest)
//        val parseDocument = Parser().parseDocument(parseRequest)
//        println(parseDocument)
        return 123
    }

    @GetMapping("/gr")
    fun tt(
        @RequestParam allRequestParams: Map<String, String>,
        @RequestParam(defaultValue = "true") test: Boolean
    ) {
        println(test)
        allRequestParams.forEach { (k, v) ->
            println("[$k: $v]")
        }
    }

    @GetMapping("/")
    fun test(
        @RequestParam allRequestParams: Map<String, String>,
        @RequestBody query: TriggerDto,
    ): Boolean {
        val query2 = "{    test {        book(id: 0.1)    }}"
        val parser = Parser()
        println(query2)

        val document = parser.parseDocument(query2)
        println(document)


        return triggerDocumentMatcher.match(document, query)
    }

    @PostMapping("/schema")
    fun test2(
        @RequestBody schema: SchemaDto,
    ): String {
        println(schema.schema)
        schemaValidator.validate(schema.schema)
        // type Book {\n    id: ID!\n    name: String!\n}\ntype Query {\n    books: [Book!]!\n}
        return "Ok"
//        return triggerDocumentMatcher.match(document, query)
    }

    @PostMapping("/redirect")
    fun redirect(@RequestBody query: JsonNode): JsonNode? {
        val parser = Parser()
        println(query)
//        val parsed = queryParser.parseRequest(query)
//        println(parsed)
//        val document: Document = parser.parseDocument(parsed)
//        println(document)
        val service = MockServiceDto(
            1, "", "http://localhost:8080/graphql", ZonedDateTime.now(),
        )
        val callRealService = redirectService.callRealService(service, query.toString())
        println(callRealService)

        return callRealService
//        return triggerDocumentMatcher.match(document, query)
    }



    @PostMapping("/compare")
    fun compareRecursive(@RequestBody query: JsonNode) {

//        val document
    }



    @GetMapping("/foo")
    fun foo() {
        println("")
    }
}
