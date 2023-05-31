package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.UserResult
import com.example.kotlindockertest.service.UserService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/mocker/{serviceName}")
    fun getMockedResponse(
        @PathVariable serviceName: String,
        @RequestBody query: JsonNode,
    ): JsonNode? {
        var userResult: UserResult
        val time = measureTimeMillis {
            userResult = userService.getResponse(serviceName, query)
        }

        val timeToSleep = userResult.requiredDelay - time / 1000
        println("Required delay = ${userResult.requiredDelay}")
        println("Execution time = $time")
        println("Will sleep for $timeToSleep seconds")
        // Todo use coroutines
        TimeUnit.SECONDS.sleep(maxOf(timeToSleep, 0))

        return userResult.response
    }
}
