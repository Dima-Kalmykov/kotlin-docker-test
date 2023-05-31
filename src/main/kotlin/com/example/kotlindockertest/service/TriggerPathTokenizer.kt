package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.trigger.FailedReason
import com.example.kotlindockertest.service.trigger.validation.TriggerTokenValidationService
import org.springframework.stereotype.Service

@Service
class TriggerPathTokenizer(
    private val triggerTokenValidationService: TriggerTokenValidationService,
) {

    fun tokenize(path: String): List<String> {
        val tokens = mutableListOf<String>()
        var notProcessedPath = path

        while (notProcessedPath.isNotEmpty()) {
            val newToken = getFirstToken(notProcessedPath)

            tokens += newToken

            val newTokenLength = notProcessedPath.indexOf(']') + 1
            notProcessedPath = notProcessedPath.drop(newTokenLength)
        }

        return tokens
    }

    private fun getFirstToken(path: String): String {
        triggerTokenValidationService.validate(path)

        val rightTokenBound = path.indexOf(']')

        if (path[1] == '\'' && path[rightTokenBound - 1] == '\'') {
            return path.substring(2, rightTokenBound - 1)
        }

        val token = path.substring(1, rightTokenBound)

        throw IllegalArgumentException(
            "${FailedReason.INVALID_TOKEN_FORMAT.reason}. Invalid token = [$token]",
        )
    }
}
