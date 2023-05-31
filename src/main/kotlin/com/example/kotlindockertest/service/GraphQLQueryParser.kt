package com.example.kotlindockertest.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class GraphQLQueryParser {

    fun parseForDocument(request: JsonNode) = parse(request)
        .replace("\\n", "")

    fun parseForHashCode(request: JsonNode) = parse(request)
        .replace("\\n", "\n")

    private fun parse(request: JsonNode) = request.get("query")
        .toString()
        .replace("\\r", "")
        .replace("\\\"", "||")
        .replace("\"", "")
        .replace("\\n", "\n")
        .replace("\t", "    ")
        .replace("\\t", "    ")
        .replace("||", "\"")
        .trim('"')
}
