package com.example.kotlindockertest.test

import com.fasterxml.jackson.databind.JsonNode

abstract class GraphQLRequestParser {

    fun parseRequest(request: JsonNode) = extractRequestBody(request)
        .toString()
        .replace("\\r\\n", "")
        .trim('"')

    protected abstract fun extractRequestBody(request: JsonNode): JsonNode
}
