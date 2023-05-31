package com.example.kotlindockertest.controller.advice

import com.example.kotlindockertest.exception.AuthorizationException
import com.example.kotlindockertest.exception.JWTTokenException
import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.exception.TriggerException
import com.example.kotlindockertest.model.StringErrorResponse
import org.postgresql.util.PSQLException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private const val INVALID_FOREIGN_KEY_PATTERN =
    "Ключ \\(service_id\\)=\\(([0-9]+)\\) отсутствует в таблице \"services\""

@RestControllerAdvice
class ExceptionHandler {

    private val regexpInvalidForeignKey = Regex(INVALID_FOREIGN_KEY_PATTERN)

    @ExceptionHandler(PSQLException::class)
    fun handleDatabaseException(ex: PSQLException): ResponseEntity<StringErrorResponse> {
        val errorMessage = requireNotNull(ex.message) { "Error message can't be null" }

        if (regexpInvalidForeignKey.find(errorMessage) != null) {
            return ResponseEntity(
                StringErrorResponse("Invalid service id. Such service doesn't exist"),
                HttpStatus.BAD_REQUEST,
            )
        }

        return ResponseEntity(StringErrorResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleMockNotFoundException(ex: NotFoundException): ResponseEntity<StringErrorResponse> {
        val errorMessage = requireNotNull(ex.message) { "Error message can't be null" }

        return ResponseEntity(
            StringErrorResponse(errorMessage),
            HttpStatus.NOT_FOUND,
        )
    }

    @ExceptionHandler(AuthorizationException::class)
    fun handleAuthorizationException(ex: AuthorizationException): ResponseEntity<StringErrorResponse> {
        val errorMessage = requireNotNull(ex.message) { "Error message can't be null" }

        return ResponseEntity(
            StringErrorResponse(errorMessage),
            HttpStatus.FORBIDDEN,
        )
    }

    @ExceptionHandler(JWTTokenException::class)
    fun handleJWTTokenException(ex: JWTTokenException): ResponseEntity<StringErrorResponse> {
        val errorMessage = requireNotNull(ex.message) { "Error message can't be null" }

        return ResponseEntity(
            StringErrorResponse(errorMessage),
            HttpStatus.UNAUTHORIZED,
        )
    }

    @ExceptionHandler(TriggerException::class)
    fun handleTriggerException(ex: TriggerException): ResponseEntity<StringErrorResponse> {
        val errorMessage = requireNotNull(ex.message) { "Error message can't be null" }

        return ResponseEntity(
            StringErrorResponse(errorMessage),
            HttpStatus.BAD_REQUEST,
        )
    }
}
