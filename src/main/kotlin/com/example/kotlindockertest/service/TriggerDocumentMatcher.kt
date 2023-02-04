package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.trigger.TriggerDto
import graphql.language.Document
import graphql.language.Field
import org.springframework.stereotype.Service

@Service
class TriggerDocumentMatcher(
    private val triggerPathTokenizer: TriggerPathTokenizer,
    private val fieldSearcher: FieldSearcher,
    private val triggerValueMatcher: TriggerValueMatcher,
) {

    // Todo может надо добавить requestCount триггер
    fun match(document: Document, trigger: TriggerDto): Boolean {
        val tokens = triggerPathTokenizer.tokenize(trigger.path)
        var currentField: Field? = null

        for (i in 0 until tokens.lastIndex) {
            val token = tokens[i]

            currentField = if (currentField == null) {
                fieldSearcher.getTopLevelField(document, token)
            } else {
                fieldSearcher.getNestedField(currentField, token) ?: return false
            }
        }

        if (currentField == null) {
            return false
        }

        return checkArgument(currentField, tokens, trigger)
    }

    private fun checkArgument(field: Field, tokens: List<String>, trigger: TriggerDto): Boolean {
        val argument = fieldSearcher.getArgument(field, tokens.last())
        println(argument)
        return if (argument != null) {
            triggerValueMatcher.match(argument, trigger)
        } else {
            false
        }
    }
}
