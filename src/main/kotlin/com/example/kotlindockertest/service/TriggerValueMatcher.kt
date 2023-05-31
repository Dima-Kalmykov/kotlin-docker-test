package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.ValueType
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.trigger.matcher.TriggerMatcher
import graphql.language.Argument
import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.StringValue
import org.springframework.stereotype.Service

@Service
class TriggerValueMatcher(private val triggerMatchers: Map<ValueType, TriggerMatcher>) {

    fun match(argument: Argument, trigger: TriggerDto): Boolean {
        val valueType = getMatcherType(argument)
        val matcher = checkNotNull(triggerMatchers[valueType]) {
            "Can't find matcher for type $valueType"
        }

        return matcher.match(argument, trigger)
    }

    private fun getMatcherType(argument: Argument): ValueType {
        return when(argument.value) {
            is StringValue -> {
                if ("." in (argument.value as StringValue).value) {
                    ValueType.FLOAT
                } else {
                    ValueType.STRING
                }
            }
            is FloatValue -> ValueType.FLOAT
            is IntValue -> ValueType.INTEGER
            else -> error("Unsupported argument type")
        }
    }
}
