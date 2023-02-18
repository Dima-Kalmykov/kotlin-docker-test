package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.trigger.TriggerDto
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.BigIntegerNode
import com.fasterxml.jackson.databind.node.DecimalNode
import com.fasterxml.jackson.databind.node.ObjectNode
import graphql.language.Argument
import graphql.language.Document
import graphql.language.Field
import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.StringValue
import org.springframework.stereotype.Service
import java.math.BigInteger


@Service
class ResponseFieldMatcher(
    private val triggerPathTokenizer: TriggerPathTokenizer,
    private val fieldSearcher: FieldSearcher,
) {

    fun match(response: ObjectNode, document: Document, path: String): ObjectNode {
        val tokens = triggerPathTokenizer.tokenize(path)
        var currentField: Field? = null
        val pathToField = mutableListOf<String?>()

        for (i in 0 until tokens.lastIndex) {
            val token = tokens[i]

            currentField = if (currentField == null) {
                val foundField = fieldSearcher.getTopLevelField(document, token)
                pathToField += foundField?.name
                foundField
            } else {
                val foundField = fieldSearcher.getNestedField(currentField, token) ?: return response
                pathToField += foundField.name
                foundField
            }
        }
        if (currentField == null) {
            return response
        }

        val argument = fieldSearcher.getArgument(currentField, tokens.last())

        var currentResponse = response
        if (argument != null) {

            pathToField.forEach { fieldName ->
                currentResponse = currentResponse.get(fieldName) as ObjectNode
            }
            if (argument.value is IntValue) {
                val intValue = (argument.value as IntValue).value
                currentResponse.set<BigIntegerNode>(argument.name, BigIntegerNode(intValue))
            }
            if (argument.value is FloatValue) {
                val floatValue = (argument.value as FloatValue).value
                currentResponse.set<DecimalNode>(argument.name, DecimalNode(floatValue))
            }
        }
        return currentResponse
    }
}
