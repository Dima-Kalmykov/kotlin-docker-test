package com.example.kotlindockertest.model.trigger

import com.example.kotlindockertest.model.OperationType
import com.example.kotlindockertest.model.ValueType
import javax.persistence.*


@Entity
@Table(
    name = "trigger",
    indexes = [
        Index(name = "i_mock_id", columnList = "mockId"),
        Index(name = "i_service_id", columnList = "serviceId"),
    ]
)
open class TriggerDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    open var path: String,

    // Todo add index
    open var mockId: Long,
    open var serviceId: Long,

    open var createdBy: String,

    @Enumerated(EnumType.STRING)
    open var operation: OperationType,

    @Enumerated(EnumType.STRING)
    open var valueType: ValueType,
    open var value: String,
    open var enable: Boolean = true,
)
