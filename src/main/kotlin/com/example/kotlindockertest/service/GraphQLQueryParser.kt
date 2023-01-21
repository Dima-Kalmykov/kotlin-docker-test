package com.example.kotlindockertest.service

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service

@Service
class GraphQLQueryParser {

    fun parseRequest(request: JsonNode) = request
        .toString()
        .replace("\\n", "")
        .trim('"')
}
