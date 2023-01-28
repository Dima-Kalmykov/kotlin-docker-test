package com.example.kotlindockertest.service.trigger.validation

import com.example.kotlindockertest.model.trigger.ValidationData


interface TriggerTokenValidator {

    fun validate(path: String): ValidationData
}
