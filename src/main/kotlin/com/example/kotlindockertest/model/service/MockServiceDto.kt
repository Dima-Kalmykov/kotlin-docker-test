package com.example.kotlindockertest.model.service

import com.example.kotlindockertest.model.mock.MockDto
import java.sql.Timestamp
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
    open var location: String,
    open var expirationDate: LocalDateTime? = null,
    open var delay: Long? = null,
    open var makeRealCall: Boolean? = null,
    open var useDefaultMock: Boolean? = null,
    @Transient
    open var mocks: List<MockDto> = emptyList(),
    @Lob
    open var schema: String? = null,
)
