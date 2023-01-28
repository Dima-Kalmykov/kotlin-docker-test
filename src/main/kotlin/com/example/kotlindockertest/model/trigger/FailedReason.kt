package com.example.kotlindockertest.model.trigger

enum class FailedReason(val reason: String) {
    INVALID_TOKEN_LENGTH("Token length must be at least 5"),
    INVALID_TOKEN_BEGINNING("Token must start with \"[\""),
    INVALID_TOKEN_ENDING("Token must end with \"]\""),
    INVALID_TOKEN_FORMAT("Token must look like \"['fieldName']\" or \"[123]\""),
}
