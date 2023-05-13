package com.example.kotlindockertest.service.history

import com.example.kotlindockertest.exception.AuthorizationException
import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.history.HistoryEventDto
import com.example.kotlindockertest.model.history.HistoryResponseDto
import com.example.kotlindockertest.model.history.HistoryShortInfo
import com.example.kotlindockertest.model.history.PaginationResult
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.repository.HistoryRepository
import com.example.kotlindockertest.service.MockServiceHandler
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class HistoryService(
    private val historyRepository: HistoryRepository,
    private val mockServiceHandler: MockServiceHandler,
) {

    @Transactional
    fun saveHistoryEvent(historyEventDto: HistoryEventDto) {
        historyRepository.save(historyEventDto)
    }

    @Transactional
    fun activateHistory(id: Long, request: ActivateRequestDto, username: String): MockServiceDto {
        val updatedService = mockServiceHandler.getService(id, username)
        if (updatedService.createdBy != username) {
            throw AuthorizationException()
        }

        updatedService.apply {
            this.storeHistory = request.enable
        }

        return updatedService
    }

    @Transactional(readOnly = true)
    fun getHistoryEvents(
        serviceId: Long,
        page: Int,
        pageSize: Int,
        from: ZonedDateTime,
        to: ZonedDateTime,
        username: String,
        isError: Boolean?,
        redirected: Boolean?,
        sortingOrder: String,
    ): HistoryResponseDto {
        if (sortingOrder != "DESC" && sortingOrder != "ASC") {
            error("Unsupported sorting order = $sortingOrder")
        }

        val service = mockServiceHandler.getService(serviceId, username)
        if (service.createdBy != username) {
            error("Can't view foreign history events")
        }
        val sorting = if (sortingOrder == "DESC") {
            Sort.by("createdAt").descending()
        } else {
            Sort.by("createdAt").ascending()
        }
        val pageRequest = PageRequest.of(page, pageSize, sorting)

        val events = when {
            isError == null && redirected == null -> historyRepository.findAllByServiceId(
                serviceId, from, to, pageRequest
            )

            isError == null && redirected != null -> historyRepository.findWithRedirected(
                serviceId, from, to, redirected, pageRequest
            )

            isError != null && redirected == null -> historyRepository.findWithError(
                serviceId, from, to, isError, pageRequest
            )

            isError != null && redirected != null -> historyRepository.findWithErrorAndRedirected(
                serviceId, from, to, isError, redirected, pageRequest
            )

            else -> error("Invalid combination of isError=$isError and redirected=$redirected")
        }

        val count = when {
            isError == null && redirected == null -> historyRepository.getCountByServiceId(
                serviceId, from, to,
            )

            isError == null && redirected != null -> historyRepository.getCountWithRedirected(
                serviceId, from, to, redirected,
            )

            isError != null && redirected == null -> historyRepository.getCountWithError(
                serviceId, from, to, isError,
            )

            isError != null && redirected != null -> historyRepository.getCountWithErrorAndRedirected(
                serviceId, from, to, isError, redirected,
            )

            else -> error("Invalid combination of isError=$isError and redirected=$redirected")
        }

        val totalPages = if (count % pageSize != 0) {
            count / pageSize + 1
        } else {
            count / pageSize
        }

        val mappedEvents = events.map { event ->
            HistoryShortInfo(
                event.id,
                event.createdAt,
                event.expirationDate,
                event.redirected,
                event.isError,
                event.request,
                event.response,
            )
        }

        return HistoryResponseDto(
            paging = PaginationResult(
                totalItems = count,
                totalPages = totalPages,
                page = page,
                pageSize = pageSize,
            ),
            items = when (sortingOrder) {
                "DESC" -> mappedEvents.sortedByDescending { it.createdAt }
                "ASC" -> mappedEvents.sortedBy { it.createdAt }
                else -> error("Unsupported sorting order $sortingOrder")
            }
        )
    }
}
