package com.example.kotlindockertest.model.trigger

class ValidationData(
    val decision: ValidationDecision,
    val failedReason: FailedReason? = null,
) {

    companion object {

        fun failed(failedReason: FailedReason) = ValidationData(
            ValidationDecision.FAILED,
            failedReason,
        )

        fun ok() = ValidationData(ValidationDecision.OK)
    }
}
