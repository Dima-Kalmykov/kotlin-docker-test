package com.example.kotlindockertest.model.service

import com.example.kotlindockertest.model.mock.MockDto
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "service")
open class MockServiceDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open var id: Long? = null,

    open var name: String,
    open var location: String,
    open var expirationDate: ZonedDateTime? = null,
    open var delay: Long? = null,
    open var createdBy: String,
    open var makeRealCall: Boolean? = null,
    @Transient
    open var mocks: List<MockDto> = emptyList(),
    @Lob
    open var schema: String? = null,
)
