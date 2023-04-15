package com.example.kotlindockertest.model.mock

import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(
    name = "mock",
    indexes = [
        Index(name = "i_request_hash", columnList = "requestHash"),
    ],
)
open class MockDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open var id: Long? = null,

    open var name: String,
    // Todo мб индекс
    open var expirationDate: ZonedDateTime? = null,
    open var delay: Long? = null, // millis
    open var enable: Boolean = true,

    @Lob
    open var request: String,

    open var requestHash: Int = request.hashCode(),

    @Lob
    open var response: String,

    // Todo может комплексный индекс
    open var serviceId: Long,
)
