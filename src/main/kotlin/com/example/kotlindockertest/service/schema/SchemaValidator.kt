package com.example.kotlindockertest.service.schema

import com.example.kotlindockertest.exception.SchemaValidationException
import graphql.language.Document
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.stereotype.Service

@Service
class SchemaValidator(
    private val schemaGenerator: SchemaGenerator,
    private val schemaParser: SchemaParser,
) {

    fun validate(schemaStringRepresentation: String): GraphQLSchema {
        val typeRegistry = schemaParser.parse(schemaStringRepresentation)

        try {
            val schema = schemaGenerator.makeExecutableSchema(typeRegistry, RuntimeWiring.MOCKED_WIRING)
            return checkNotNull(schema) { "Graphql schema can't be null" }
        } catch (exception: Exception) {
            throw SchemaValidationException(exception.message)
        }
    }
}
