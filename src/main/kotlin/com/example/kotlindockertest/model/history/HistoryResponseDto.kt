package com.example.kotlindockertest.model.history

import java.time.ZonedDateTime
import javax.persistence.Lob

data class HistoryResponseDto(
    val paging: PaginationResult,
    val items: List<HistoryShortInfo>,
)

data class PaginationResult(
    val totalItems: Int,
    val totalPages: Int,
    val page: Int,
    val pageSize: Int,
)

data class HistoryShortInfo(
    val id: Long? = null,
    val createdAt: ZonedDateTime? = null,
    val expirationDate: ZonedDateTime? = null,
    val redirected: Boolean = false,
    val isError: Boolean = false,
    val request: String = "",
    val response: String = "",
)
