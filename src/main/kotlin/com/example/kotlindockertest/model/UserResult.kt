package com.example.kotlindockertest.model

import com.fasterxml.jackson.databind.JsonNode

data class UserResult(
    val response: JsonNode?,
    val requiredDelay: Long,
)
