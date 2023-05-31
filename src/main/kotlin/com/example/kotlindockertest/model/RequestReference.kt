package com.example.kotlindockertest.model

import javax.persistence.*


//@Entity
//@Table(name = "request_reference")
open class RequestReference(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long,

    open var responsePath: String,
    open var requestPath: String,

    // Todo foreign key
    open var mockId: Long,
)
