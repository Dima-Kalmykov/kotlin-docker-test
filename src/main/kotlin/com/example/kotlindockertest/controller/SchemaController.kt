package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.SchemaDto
import com.example.kotlindockertest.service.schema.SchemaValidator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/graphql/validation")
class SchemaController(private val schemaValidator: SchemaValidator) {

    @PostMapping("/")
    fun validate(@RequestBody schema: SchemaDto): SchemaDto {
        schemaValidator.validate(schema.schema)

        return schema
    }
}
