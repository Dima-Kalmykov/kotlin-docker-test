package com.example.kotlindockertest.test

import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.FieldSearcher
import com.example.kotlindockertest.service.GraphQLQueryParser
import com.example.kotlindockertest.service.TriggerDocumentMatcher
import com.fasterxml.jackson.databind.JsonNode
import graphql.GraphQL
import graphql.language.*
import graphql.parser.Parser
import graphql.schema.GraphQLSchema
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val fieldSearcher: FieldSearcher,
    private val queryParser: GraphQLQueryParser,
    private val triggerDocumentMatcher: TriggerDocumentMatcher,
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
    ) {
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

    @PostMapping("/g")
    fun test2(
        @RequestParam allRequestParams: Map<String, String>,
        @RequestBody query: JsonNode,
    ): String {
        val parser = Parser()
        println(query.toString())
        val parsed = queryParser.parseRequest(query)
        println(parsed)
        val document = parser.parseDocument(parsed)
        println(document)


        return "Ok"
//        return triggerDocumentMatcher.match(document, query)
    }
}
