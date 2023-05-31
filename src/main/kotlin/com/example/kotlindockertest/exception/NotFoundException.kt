package com.example.kotlindockertest.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class NotFoundException(message: String) :
    ResponseStatusException(HttpStatus.NOT_FOUND, message)
