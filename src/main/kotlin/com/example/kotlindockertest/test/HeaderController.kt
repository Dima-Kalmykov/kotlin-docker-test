package com.example.kotlindockertest.test

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//@RestController
//@RequestMapping("/header")
class HeaderController {

    @PostMapping("/test")
    fun testMapping(@RequestAttribute username: String): String {
        return "200"
    }
}