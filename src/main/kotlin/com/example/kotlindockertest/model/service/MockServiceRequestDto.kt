package com.example.kotlindockertest.model.service

import java.time.LocalDateTime
import java.time.ZonedDateTime

open class MockServiceRequestDto(
    open var name: String,
    open var location: String,
    open var expirationDate: ZonedDateTime? = null,
    open var delay: Long? = null,
    open var makeRealCall: Boolean? = null,
    open var schema: String? = null,
    open var historyStorageDuration: Long = 1440 * 30,
    open var storeHistory: Boolean? = null,
)
