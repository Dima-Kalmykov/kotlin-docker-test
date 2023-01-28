package com.example.kotlindockertest.service.trigger.validation

import com.example.kotlindockertest.model.trigger.FailedReason
import com.example.kotlindockertest.model.trigger.ValidationData
import com.example.kotlindockertest.model.trigger.ValidationDecision
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Service
@Order(value = 2)
class TriggerTokenFormatValidator : TriggerTokenValidator {

    override fun validate(path: String): ValidationData {
        if (path[0] != '[') {
            return ValidationData.failed(FailedReason.INVALID_TOKEN_BEGINNING)
        }

        val rightTokenBound = path.indexOf(']')
        if (rightTokenBound == -1) {
            return ValidationData.failed(FailedReason.INVALID_TOKEN_ENDING)
        }

        return ValidationData(ValidationDecision.OK)
    }
}
