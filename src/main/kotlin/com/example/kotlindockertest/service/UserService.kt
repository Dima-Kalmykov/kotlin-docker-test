package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.RedirectException
import com.example.kotlindockertest.model.UserResult
import com.example.kotlindockertest.model.history.HistoryEventDto
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.document.DocumentComparator
import com.example.kotlindockertest.service.history.HistoryService
import com.example.kotlindockertest.service.mock.MockRandomFieldsValidator
import com.example.kotlindockertest.service.mock.MockService
import com.example.kotlindockertest.service.schema.DocumentValidator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import graphql.language.Document
import graphql.parser.Parser
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

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
    private val randomFieldsValidator: MockRandomFieldsValidator,
    private val historyService: HistoryService,
) {

    fun getResponse(
        serviceName: String,
        query: JsonNode,
    ): UserResult {
        val historyEventDto = HistoryEventDto()

        val service = mockServiceHandler.getServiceByName(serviceName)
        if (service.storeHistory == true) {
            historyEventDto.serviceId = service.id
            historyEventDto.expirationDate = ZonedDateTime.now().plusMinutes(service.historyStorageDuration)
            historyEventDto.createdAt = ZonedDateTime.now()
        }
        val document: Document?
        val mock: MockDto?
        try {
            val parsedQuery = graphQLQueryParser.parseForDocument(query)
            println("Input query = $parsedQuery")

            if (service.storeHistory == true) {
                historyEventDto.request = parsedQuery
            }
            document = documentParser.parseDocument(parsedQuery)

            validateSchemaIfExist(service, document)

            mock = mockService.getMockByRequestHash(
                service.id!!,
                graphQLQueryParser.parseForHashCode(query).hashCode(),
            )
        } catch (exception: RuntimeException) {
            if (service.storeHistory == true) {
                historyEventDto.isError = true
                historyEventDto.response = exception.message!!
                historyService.saveHistoryEvent(historyEventDto)
            }
            throw exception
        }

        val delay = getDelay(mock, service)

        return when {
            mock != null && !mock.enable -> {
                try {
                    val userResult = checkDefaultFlow(service, query, delay)
                    if (service.storeHistory == true) {
                        historyEventDto.response = userResult.response.toString()
                        historyEventDto.redirected = true
                        historyService.saveHistoryEvent(historyEventDto)
                    }

                    userResult
                } catch (redirectException: RedirectException) {
                    if (service.storeHistory == true) {
                        historyEventDto.redirected = true
                        historyEventDto.isError = true
                        historyEventDto.response = redirectException.message!!
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    throw redirectException
                } catch (exception: Exception) {
                    if (service.storeHistory == true) {
                        historyEventDto.isError = true
                        historyEventDto.response = exception.message!!
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    throw exception
                }
            }

            mock != null -> {
                try {
                    val jsonResponse = randomFieldsValidator.validateRandomFields(mock.response)
                    val userResult = UserResult(jsonResponse, delay)
                    if (service.storeHistory == true) {
                        historyEventDto.response = userResult.response.toString()
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    userResult
                } catch (e: RuntimeException) {
                    if (service.storeHistory == true) {
                        historyEventDto.response = e.message!!
                        historyEventDto.isError = true
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    throw e
                }
            }

            else -> {
                try {
                    val userResult = checkTriggers(service, document!!, query, delay)
                    if (service.storeHistory == true) {
                        historyEventDto.response = userResult.response.toString()
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    userResult
                } catch (redirectException: RedirectException) {
                    if (service.storeHistory == true) {
                        historyEventDto.redirected = true
                        historyEventDto.isError = true
                        historyEventDto.response = redirectException.message!!
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    throw redirectException
                } catch (exception: RuntimeException) {
                    if (service.storeHistory == true) {
                        historyEventDto.isError = true
                        historyEventDto.response = exception.message!!
                        historyService.saveHistoryEvent(historyEventDto)
                    }
                    throw exception
                }
            }
        }
    }

    private fun checkTriggers(
        service: MockServiceDto,
        document: Document,
        query: JsonNode,
        delay: Long,
    ): UserResult {
        val mocks = mockService.getMocks(service.id!!, "", true)
        val triggers = mocks.flatMap { mock -> triggerService.getTriggers(mock.id!!) }
        val visited = triggers.associate { it.mockId to false }.toMutableMap()

        triggers.forEach {
            val mockId = it.mockId

            if (visited[mockId] == false) {
                val mockByTrigger = mockService.getMock(mockId, "", true)

                val mockDocument = documentParser.parseDocument(mockByTrigger.request)
                if (documentComparator.areEqual(document, mockDocument)) {
                    if (triggers.match(document, mockId)) {
                        val jsonResponse = randomFieldsValidator.validateRandomFields(mockByTrigger.response)
                        UserResult(jsonResponse, delay)

                        return UserResult(jsonResponse, delay)
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
