package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.ValueType
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.trigger.matcher.TriggerMatcher
import graphql.language.Argument
import org.springframework.stereotype.Service

@Service
class TriggerValueMatcher(private val triggerMatchers: Map<ValueType, TriggerMatcher>) {

    fun match(argument: Argument, trigger: TriggerDto): Boolean {
        val matcher = checkNotNull(triggerMatchers[trigger.valueType]) {
            "Can't find matcher for type ${trigger.valueType}"
        }

        return matcher.match(argument, trigger)
    }
}
