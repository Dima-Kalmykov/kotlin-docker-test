package com.example.kotlindockertest.model.mock

import javax.persistence.*

@Entity
@Table(
    name = "mock",
    indexes = [
        Index(name = "i_request_hash", columnList = "requestHash", unique = true),
    ],
)
open class MockDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    open var name: String,
    open var ttl: Long,
    open var delay: Long? = null, // millis

    @Lob
    open var request: String,

    open var requestHash: Int = request.hashCode(),

    @Lob
    open var response: String,

    // Todo может комплексный индекс
    open var serviceId: Long,
)
