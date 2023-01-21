package com.example.kotlindockertest.model.service

import javax.persistence.*

@Entity
@Table(name = "service")
open class MockServiceDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    @Column(unique = true)
    open var name: String,
    open var host: String,
    open var ttl: Long,
    open var delay: Long = 0,
    open var location: String? = null,
    open var makeRealCall: Boolean? = null,
    open var defaultMockId: Long? = null,
)
