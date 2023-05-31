package com.example.kotlindockertest.service.trigger.validation

import com.example.kotlindockertest.model.trigger.ValidationDecision
import org.springframework.stereotype.Service

@Service
class TriggerTokenValidationService(private val validators: List<TriggerTokenValidator>) {

    fun validate(token: String) {
        validators.forEach {
            val validationResult = it.validate(token)

            if (validationResult.decision == ValidationDecision.FAILED) {
                throw IllegalArgumentException(
                    "${validationResult.failedReason?.reason}. Invalid token = $token"
                )
            }
        }
    }
}
