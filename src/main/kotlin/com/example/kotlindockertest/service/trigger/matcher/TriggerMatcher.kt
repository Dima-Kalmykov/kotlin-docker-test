package com.example.kotlindockertest.service.trigger.matcher

import com.example.kotlindockertest.model.ValueType
import com.example.kotlindockertest.model.trigger.TriggerDto
import graphql.language.Argument

interface TriggerMatcher {

    val valueType: ValueType

    fun match(argument: Argument, trigger: TriggerDto): Boolean
}
