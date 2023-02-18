package com.example.kotlindockertest.service

import com.example.kotlindockertest.repository.RequestReferenceRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.stereotype.Service

@Service
class RequestReferenceService(
    private val requestReferenceRepository: RequestReferenceRepository,
    private val triggerPathTokenizer: TriggerPathTokenizer,
) {

    // Todo rename
    fun todo(mockId: Long, request: JsonNode, response: JsonNode) {
        val references = requestReferenceRepository.findAllByMockId(mockId)
        references.forEach { reference ->
            val requestValue = getValueOfRequest(reference.requestPath, request)
            val tokens = triggerPathTokenizer.tokenize(reference.responsePath)

        }
    }

    private fun getValueOfRequest(requestPath: String, request: JsonNode): JsonNode? {
        val tokens = triggerPathTokenizer.tokenize(requestPath)

        var currentField = request
        tokens.forEach { token ->
            currentField = currentField.get(token) ?: return null
        }

        return currentField
    }

    private fun setRequestValueToResponse(responsePath: String, requestValue: JsonNode, response: JsonNode): ObjectNode? {
        val tokens = triggerPathTokenizer.tokenize(responsePath)

        var actualResponse = response as  ObjectNode
        tokens.forEach { token ->
            actualResponse = actualResponse.get(token) as ObjectNode? ?: return null
        }

        return actualResponse
//        actualResponse.set()
    }
}
