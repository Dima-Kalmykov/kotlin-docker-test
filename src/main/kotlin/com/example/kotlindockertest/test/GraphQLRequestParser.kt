package com.example.kotlindockertest.test

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class GraphQLRequestParser {

    fun parseRequest(request: JsonNode) = extractRequestBody(request)
        .toString()
        .replace("\\r\\n", "")
        .trim('"')

    protected fun extractRequestBody(request: JsonNode) = request.get("query")
}
