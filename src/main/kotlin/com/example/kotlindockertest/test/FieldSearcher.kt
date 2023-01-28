package com.example.kotlindockertest.test

import graphql.language.*
import org.springframework.stereotype.Service

const val A = ""

@Service
class FieldSearcher {

    fun getField(document: Document, fieldName: String): Field? {
        document.definitions.forEach { definition ->
            val operationDefinition = definition as OperationDefinition

            return operationDefinition.selectionSet.selections.find { (it as Field).name == fieldName } as Field
        }

        return null
    }

    fun getNestedField(field: Field, nestedFieldName: String): Field? {
        return field.selectionSet.selections.find { (it as Field).name == nestedFieldName } as Field
    }


    fun findField(document: Document, fieldName: String): Field? {
        document.definitions.forEach { definition ->
            val operationDefinition = definition as OperationDefinition

            return operationDefinition.selectionSet.findField(fieldName) ?: return@forEach
        }

        return null
    }

    private fun SelectionSet.findField(fieldName: String): Field? {
        selections?.forEach { selection ->
            if (selection.isFieldWithName(fieldName)) {
                return selection as Field
            }

            selection.findFieldAmongChildren(fieldName)?.let { return it }
        }

        return null
    }

    private fun Selection<*>.isFieldWithName(fieldName: String) =
        (this as Field).name.equals(fieldName, ignoreCase = true)


    private fun Selection<*>.findFieldAmongChildren(fieldName: String): Field? {
        children?.forEach { child ->
            val childSelection = child as SelectionSet

            return childSelection.findField(fieldName) ?: return@forEach
        }

        return null
    }
}