package com.example.kotlindockertest.test

import com.example.kotlindockertest.service.GraphQLQueryParser
import com.fasterxml.jackson.databind.JsonNode
import graphql.language.*
import graphql.parser.Parser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val fieldSearcher: FieldSearcher,
    private val queryParser: GraphQLQueryParser,
) {

    companion object : Thread()

    data class Parent(val name: String, val age: Int)

    @PostMapping("/test")
    fun test2(@RequestBody body: JsonNode): Int {
        println("Got request with query ${body.toPrettyString()}")
        val parseRequest = queryParser.parseRequest(body.get("value1"))
        println(parseRequest)
        val parseDocument = Parser().parseDocument(parseRequest)
        println(parseDocument)
        return 123
    }

    @GetMapping("/")
    fun test(
        @RequestParam allRequestParams: Map<String, String>,
        @RequestBody query: JsonNode,
    ): String {
        val parser = Parser()
        println(query.toPrettyString())
        val parsed = queryParser.parseRequest(query)
        var changed = parsed
        allRequestParams.forEach { (key, value) ->
            changed = changed.replace("__${key}__", value)
        }
        println(changed.length)
        val document = parser.parseDocument(queryParser.parseRequest(query))
        println(document)
        println(document.toString().length)
        println(document.toString())
        val foundField = fieldSearcher.findField(document, "dima")

        println("Found field $foundField")

        return "Ok"
    }
}
