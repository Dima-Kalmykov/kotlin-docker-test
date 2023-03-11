package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.model.UserResult
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.service.schema.DocumentValidator
import com.example.kotlindockertest.service.schema.SchemaValidator
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
    private val documentValidator: DocumentValidator,
    private val documentParser: Parser,
) {

    fun getResponse(
        serviceName: String,
        identicalComparison: Boolean,
        query: JsonNode,
    ): UserResult {
        val service = mockServiceHandler.getServiceByName(serviceName)
        // Todo можно тупо вытаскивать все триггеры сразу
        val parsedQuery = graphQLQueryParser.parseRequest(query)
        val document = documentParser.parseDocument(parsedQuery)

        service.schema?.let {
            documentValidator.validate(document, it)
        }

        return if (identicalComparison) {
            val mock = mockService.getMockByRequestHash(service.id, parsedQuery.hashCode())
            val delay = getDelay(mock, service)
            if (mock == null) {
                checkDefaultFlow(service, query, delay)
            } else {
                UserResult(mock.response.toJson(), delay)
            }
        } else {
            // Todo fix
            val mock = mockService.getMockByName(service.id, "mockName")
            val delay = getDelay(mock, service)
            val result = checkTriggers(parsedQuery, mock, delay)
            if (result.response == null) {
                checkDefaultFlow(service, query, delay, mock)
            } else {
                result
            }
        }
    }

    private fun getDelay(mock: MockDto?, service: MockServiceDto) =
        mock?.delay ?: service.delay ?: error("Unspecified delay")

    private fun checkDefaultFlow(service: MockServiceDto, query: JsonNode, delay: Long): UserResult {
        return if (service.makeRealCall == true) {
            val response = redirectService.callRealService(service, query.toString())
            UserResult(response, delay)
        } else {
            error("Service with name ${service.name} doesn't contain suitable mock")
        }
    }

    private fun checkDefaultFlow(service: MockServiceDto, query: JsonNode, delay: Long, mock: MockDto): UserResult {
        return when {
            service.makeRealCall == true -> {
                val response = redirectService.callRealService(service, query.toString())
                UserResult(response, delay)
            }

            service.useDefaultMock == true -> {
                UserResult(mock.response.toJson(), delay)
            }

            else -> throw NotFoundException("Mock with service id = ${service.id} and suitable body not found")
        }
    }

    private fun checkTriggers(parsedQuery: String, mock: MockDto, delay: Long): UserResult {
        val document = documentParser.parseDocument(parsedQuery)

        val triggers = triggerService.getTriggers(mock.id)

        val responseBody = triggers.firstOrNull { trigger ->
            triggerDocumentMatcher.match(document, trigger)
        }?.response

        return UserResult(responseBody?.toJson(), delay)
    }

    private fun String.toJson() = objectMapper.readTree(this)
}
