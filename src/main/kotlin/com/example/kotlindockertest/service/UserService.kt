package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.UserResult
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.document.DocumentComparator
import com.example.kotlindockertest.service.schema.DocumentValidator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.language.Document
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
    private val documentComparator: DocumentComparator,
) {

    fun getResponse(
        serviceName: String,
        query: JsonNode,
    ): UserResult {
        val service = mockServiceHandler.getServiceByName(serviceName)
        println("Input query = ${query}")
        val parsedQuery = graphQLQueryParser.parseForDocument(query)
        println("Parsed = $parsedQuery")
        val document = documentParser.parseDocument(parsedQuery)

//        validateSchemaIfExist(service, document)

        val mock = mockService.getMockByRequestHash(
            service.id!!,
            graphQLQueryParser.parseForHashCode(query).hashCode(),
        )

        val delay = getDelay(mock, service)

        return when {
            mock != null && !mock.enable -> checkDefaultFlow(service, query, delay)

            mock != null -> UserResult(mock.response.toJson(), delay)

            else -> checkTriggers(service, document, query, delay)
        }
    }

    private fun checkTriggers(
        service: MockServiceDto,
        document: Document,
        query: JsonNode,
        delay: Long,
    ): UserResult {
        val triggers = triggerService.getTriggersByServiceId(service.id!!)
        val visited = triggers.associate { it.mockId to false }.toMutableMap()

        triggers.forEach {
            val mockId = it.mockId

            if (visited[mockId] == false) {
                val mockByTrigger = mockService.getMock(mockId)

                val mockDocument = documentParser.parseDocument(mockByTrigger.request)
                println("Doc = $document")
                println("MockDO = $mockDocument")
                println("-".repeat(100))
                if (documentComparator.areEqual(document, mockDocument)) {
                    println("Yes, equals")
                    if (triggers.match(document, mockId)) {
                        return UserResult(mockByTrigger.response.toJson(), delay)
                    } else {
                        visited[mockId] = true
                    }
                } else {
                    visited[mockId] = true
                }
            }
        }

        return checkDefaultFlow(service, query, delay)
    }

    private fun List<TriggerDto>.match(document: Document, mockId: Long) = this
        .filter { it.mockId == mockId }
        .any { trigger ->
            triggerDocumentMatcher.match(document, trigger)
        }

    private fun validateSchemaIfExist(
        service: MockServiceDto,
        document: Document,
    ) {
        service.schema?.let {
            if (it.isNotEmpty()) {
                documentValidator.validate(document, it)
            }
        }
    }

    private fun getDelay(mock: MockDto?, service: MockServiceDto) =
        mock?.delay ?: service.delay ?: 0

    private fun checkDefaultFlow(service: MockServiceDto, query: JsonNode, delay: Long): UserResult {
        return if (service.makeRealCall == true) {
            val response = redirectService.callRealService(service, query.toString())
            UserResult(response, delay)
        } else {
            error("Service with name ${service.name} doesn't contain suitable mock")
        }
    }

    private fun String.toJson() = objectMapper.readTree(this)
}
