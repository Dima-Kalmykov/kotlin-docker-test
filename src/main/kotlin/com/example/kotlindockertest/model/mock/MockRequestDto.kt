package com.example.kotlindockertest.model.mock

import java.time.LocalDateTime
import java.time.ZonedDateTime

open class MockRequestDto(
    open var name: String,
    open var expirationDate: ZonedDateTime? = null,
    open var delay: Long? = null, // millis
    open var enable: Boolean = true,
    open var request: String,
    open var response: String,
    open var serviceId: Long,
)
