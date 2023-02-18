package com.example.kotlindockertest.model.trigger

import com.example.kotlindockertest.model.OperationType
import com.example.kotlindockertest.model.ValueType
import javax.persistence.*


@Entity
@Table(name = "trigger")
open class TriggerDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    @Lob
    open var response: String,

    open var path: String,

    // Todo add index
    open var mockId: Long,

    @Enumerated(EnumType.STRING)
    open var operation: OperationType,

    @Enumerated(EnumType.STRING)
    open var valueType: ValueType,
    open var value: String,
    open var enable: Boolean = true,
)
