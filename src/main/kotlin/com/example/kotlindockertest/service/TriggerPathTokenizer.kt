package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.trigger.FailedReason
import com.example.kotlindockertest.service.trigger.validation.TriggerTokenValidationService
import org.springframework.stereotype.Service

@Service
class TriggerPathTokenizer(
    private val triggerTokenValidationService: TriggerTokenValidationService,
) {

    fun tokenize(path: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var notProcessedPath = path

        while (notProcessedPath.isNotEmpty()) {
            val newToken = getFirstToken(notProcessedPath)

            tokens += newToken

            val newTokenLength = notProcessedPath.indexOf(']') + 1
            notProcessedPath = notProcessedPath.drop(newTokenLength)
        }

        return tokens
    }

    private fun getFirstToken(path: String): Token {
        triggerTokenValidationService.validate(path)

        val rightTokenBound = path.indexOf(']')

        if (path[1] == '\'' && path[rightTokenBound - 1] == '\'') {
            val token = path.substring(2, rightTokenBound - 1)
            return Token(TokenType.FIELD_NAME, token)
        }

        val token = path.substring(1, rightTokenBound)

        if (token.isNumber()) {
            return Token(
                TokenType.ARRAY_INDEX,
                token.toLong(),
            )
        }

        throw IllegalArgumentException(
            "${FailedReason.INVALID_TOKEN_FORMAT.reason}. Invalid token = [$token]",
        )
    }

    private fun String.isNumber() = all { it.isDigit() }
}


enum class TokenType {
    ARRAY_INDEX,
    FIELD_NAME,
}

class Token(
    val tokenType: TokenType,
    val value: Any,
) {

    override fun toString(): String {
        return "[type = $tokenType, value = $value]"
    }
}
