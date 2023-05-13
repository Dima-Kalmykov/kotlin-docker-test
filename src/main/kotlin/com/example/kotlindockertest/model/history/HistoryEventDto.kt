package com.example.kotlindockertest.model.history

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "history")
open class HistoryEventDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open var id: Long? = null,

    open var serviceId: Long? = null,
    open var createdAt: ZonedDateTime? = null,
    open var expirationDate: ZonedDateTime? = null,

    open var redirected: Boolean = false,
    open var isError: Boolean = false,

    @Lob
    open var request: String = "",

    @Lob
    open var response: String = "",
)
