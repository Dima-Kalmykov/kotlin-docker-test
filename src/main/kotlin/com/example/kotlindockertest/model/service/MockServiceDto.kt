package com.example.kotlindockertest.model.service

import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
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
    @Transient
    open var ttl: Long,
    open var ttlDateTime: LocalDateTime? = null,
    open var delay: Long? = null,
    open var location: String? = null, // Todo must start with /
    open var makeRealCall: Boolean? = null,
    open var defaultMockId: Long? = null,
)
