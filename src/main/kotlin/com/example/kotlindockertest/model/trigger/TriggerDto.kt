package com.example.kotlindockertest.model.trigger

import javax.persistence.*


@Entity
@Table(name = "trigger")
open class TriggerDto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    open var path: String,
    open var mockId: Long,
)
