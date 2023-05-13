package com.example.kotlindockertest.model.trigger

import com.example.kotlindockertest.model.OperationType
import com.example.kotlindockertest.model.ValueType
import javax.persistence.*


@Entity
@Table(
    name = "trigger",
    indexes = [
        Index(name = "i_mock_id", columnList = "mockId"),
    ]
)
open class TriggerDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    open var path: String,

    // Todo add index
    open var mockId: Long,

    open var createdBy: String? = null,

    @Enumerated(EnumType.STRING)
    open var operation: OperationType,

    open var value: String,
    open var enable: Boolean = true,
)
