package com.example.kotlindockertest.service.schema

import com.example.kotlindockertest.exception.DocumentValidationException
import com.example.kotlindockertest.exception.SchemaValidationException
import graphql.analysis.QueryTraverser
import graphql.analysis.QueryVisitor
import graphql.language.Document
import graphql.schema.GraphQLSchema
import org.springframework.stereotype.Service

@Service
class DocumentValidator(
    private val queryVisitor: QueryVisitor,
    private val schemaValidator: SchemaValidator,
) {

    fun validate(document: Document, schemaStringRepresentation: String) {
        val schema = schemaValidator.validate(schemaStringRepresentation)
        val traverser = buildTraverser(document, schema)

        try {
            traverser.visitDepthFirst(queryVisitor)
        } catch (exception: Exception) {
            throw DocumentValidationException(exception.message)
        }
    }

    private fun buildTraverser(document: Document, schema: GraphQLSchema) = QueryTraverser
        .newQueryTraverser()
        .document(document)
        .schema(schema)
        .build()
}