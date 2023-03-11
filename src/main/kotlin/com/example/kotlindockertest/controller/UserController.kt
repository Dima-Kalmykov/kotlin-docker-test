package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.UserResult
import com.example.kotlindockertest.service.UserService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/mocker/{serviceName}")
    fun getMockedResponse(
        @PathVariable serviceName: String,
        @RequestParam(defaultValue = false.toString()) identicalComparison: Boolean,
        @RequestBody query: JsonNode,
    ): JsonNode? {
        var userResult: UserResult
        val time = measureTimeMillis {
            userResult = userService.getResponse(serviceName, identicalComparison, query)
        }

        // Todo use coroutines
        TimeUnit.MILLISECONDS.sleep(maxOf(userResult.requiredDelayMills - time, 0))

        return userResult.response
    }
}


 data class QueryWrapper(val query: String)
