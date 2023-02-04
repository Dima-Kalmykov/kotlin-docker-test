package com.example.kotlindockertest.service

import graphql.language.*
import org.springframework.stereotype.Service

@Service
class FieldSearcher {

    fun getTopLevelField(document: Document, fieldName: String): Field? {
        document.definitions.forEach { definition ->
            val operationDefinition = definition as OperationDefinition

            return operationDefinition.selectionSet?.selections
                ?.find { (it as Field).name == fieldName } as Field?
        }

        return null
    }

    fun getNestedField(field: Field, nestedFieldName: String): Field? {
        return field.selectionSet?.selections?.find { (it as Field).name == nestedFieldName } as Field?
    }

    fun getArgument(field: Field, argumentName: String): Argument? {
        return field.arguments.find { it.name == argumentName }
    }
}