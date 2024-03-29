package com.example.kotlindockertest.service.trigger.matcher

import com.example.kotlindockertest.model.OperationType
import com.example.kotlindockertest.model.ValueType
import com.example.kotlindockertest.model.trigger.TriggerDto
import graphql.language.Argument
import graphql.language.IntValue
import org.springframework.stereotype.Service

@Service
class TriggerIntegerMatcher : TriggerMatcher {

    override val valueType = ValueType.INTEGER

    override fun match(argument: Argument, trigger: TriggerDto): Boolean {
        val argumentValue = (argument.value as IntValue).value
        val triggerValue = trigger.value.toBigInteger() 
            
        return when (trigger.operation) {
            OperationType.EQUAL -> argumentValue == triggerValue
            OperationType.LESS -> argumentValue < triggerValue
            OperationType.LESS_OR_EQUAL -> argumentValue <= triggerValue
            OperationType.GREATER -> argumentValue > triggerValue
            OperationType.GREATER_OR_EQUAL -> argumentValue >= triggerValue
            OperationType.REGEX -> error("Unsupported REGEX operation fot int values")
        }
    }
}
