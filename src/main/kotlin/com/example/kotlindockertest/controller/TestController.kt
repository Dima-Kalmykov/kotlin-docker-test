package com.example.kotlindockertest.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/hello")
    fun hello(): String {
        println("hello")
        val a = 2
        return "23"
    }

    @GetMapping("/test")
    fun test(): String {
        println("test")
        val a = 2
        return "123"
    }
}
