package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.history.HistoryResponseDto
import com.example.kotlindockertest.service.history.HistoryService
import org.springframework.web.bind.annotation.*
import java.time.ZoneId
import java.time.ZonedDateTime

@RestController
@RequestMapping("/graphql")
class HistoryController(private val historyService: HistoryService) {

    @GetMapping("/services/{id}/history")
    fun getHistoryEvents(
        @PathVariable id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "") from: String,
        @RequestParam(defaultValue = "") to: String,
        @RequestParam(required = false) isError: Boolean? = null,
        @RequestParam(required = false) redirected: Boolean? = null,
        @RequestParam(defaultValue = "DESC") sortingOrder: String,
        @RequestAttribute username: String,
    ): HistoryResponseDto {
        val dateFrom = if (from.isEmpty()) {
            ZonedDateTime.of(2022, 1, 1, 1, 1, 1, 1, ZoneId.systemDefault())
        } else {
            ZonedDateTime.parse(from)
        }
        val dateTo = if (to.isEmpty()) {
            ZonedDateTime.of(2024, 1, 1, 1, 1, 1, 1, ZoneId.systemDefault())
        } else {
            ZonedDateTime.parse(to)
        }
        return historyService.getHistoryEvents(
            id, page, pageSize, dateFrom, dateTo, username, isError, redirected, sortingOrder,
        )
    }
}
