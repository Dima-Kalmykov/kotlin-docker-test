package com.example.kotlindockertest.model.mock

import javax.persistence.*

@Entity
@Table(name = "mock")
open class MockDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    open var name: String,
    open var ttl: Long,
    open var delay: Long = 0,

    @Lob
    open var request: String,

    @Lob
    open var response: String,

    open var serviceId: Long,
)
