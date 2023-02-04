package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.model.UserResult
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.parser.Parser
import org.springframework.stereotype.Service

@Service
class UserService(
    private val triggerDocumentMatcher: TriggerDocumentMatcher,
    private val mockServiceHandler: MockServiceHandler,
    private val graphQLQueryParser: GraphQLQueryParser,
    private val redirectService: RedirectService,
    private val triggerService: TriggerService,
    private val objectMapper: ObjectMapper,
    private val mockService: MockService,
) {

    private val documentParser = Parser()

    fun getResponse(
        serviceName: String,
        query: JsonNode,
    ): UserResult {
        val service = mockServiceHandler.getServiceByName(serviceName)

        val parsedQuery = graphQLQueryParser.parseRequest(query)
        val mock = mockService.getMockByRequestHash(service.id, parsedQuery.hashCode())

        val delay = getDelay(mock, service)
        if (mock == null) {
            return checkDefaultFlow(service, query, delay)
        }

        return checkTriggers(parsedQuery, mock, delay)
    }

    private fun getDelay(mock: MockDto?, service: MockServiceDto) =
        mock?.delay ?: service.delay ?: error("Unspecified delay")

    private fun checkDefaultFlow(service: MockServiceDto, query: JsonNode, delay: Long): UserResult {
        return when {
            service.makeRealCall == true -> {
                val response = redirectService.callRealService(service, query)
                UserResult(response, delay)
            }

            service.defaultMockId != null -> {
                val defaultMock = mockServiceHandler.getDefaultMock(service.defaultMockId!!)
                UserResult(defaultMock.response.toJson(), delay)
            }

            else -> throw NotFoundException("Mock with service id = ${service.id} and suitable body not found")
        }
    }

    private fun checkTriggers(parsedQuery: String, mock: MockDto, delay: Long): UserResult {
        val document = documentParser.parseDocument(parsedQuery)

        val triggers = triggerService.getTriggers(mock.id)

        val responseBody = triggers.firstOrNull { trigger ->
            triggerDocumentMatcher.match(document, trigger)
        }?.response ?: mock.response

        return UserResult(responseBody.toJson(), delay)
    }

    private fun String.toJson() = objectMapper.readTree(this)
}
