package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.SchemaDto
import com.example.kotlindockertest.service.schema.SchemaValidator
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/graphql/validation")
class SchemaController(private val schemaValidator: SchemaValidator) {

    @PostMapping
    fun validate(@RequestBody schema: SchemaDto): SchemaDto {
        try {
            schemaValidator.validate(schema.schema)
        } catch (e: RuntimeException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }

        return schema
    }
}
