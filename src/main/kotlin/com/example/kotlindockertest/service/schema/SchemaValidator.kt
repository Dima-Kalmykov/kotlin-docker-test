package com.example.kotlindockertest.service.schema

import com.example.kotlindockertest.exception.ValidationException
import graphql.analysis.QueryTraverser
import graphql.analysis.QueryVisitor
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
    private val queryVisitor: QueryVisitor,
) {

    fun validate(document: Document, schemaStringRepresentation: String) {
        val typeRegistry = schemaParser.parse(schemaStringRepresentation)
        val schema = schemaGenerator.makeExecutableSchema(typeRegistry, RuntimeWiring.MOCKED_WIRING)

        val traverser = buildTraverser(document, schema)

        try {
            traverser.visitDepthFirst(queryVisitor)
        } catch (exception: Exception) {
            throw ValidationException(exception.message)
        }
    }

    private fun buildTraverser(document: Document, schema: GraphQLSchema) = QueryTraverser
        .newQueryTraverser()
        .document(document)
        .schema(schema)
        .build()
}
