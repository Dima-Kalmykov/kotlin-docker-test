package com.example.kotlindockertest.service.trigger.validation

import com.example.kotlindockertest.model.trigger.FailedReason
import com.example.kotlindockertest.model.trigger.ValidationData
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Service
@Order(value = 1)
class TriggerTokenLengthValidator : TriggerTokenValidator {

    override fun validate(path: String): ValidationData {
        if (path.length < 5) {
            return ValidationData.failed(FailedReason.INVALID_TOKEN_LENGTH)
        }

        return ValidationData.ok()
    }
}
