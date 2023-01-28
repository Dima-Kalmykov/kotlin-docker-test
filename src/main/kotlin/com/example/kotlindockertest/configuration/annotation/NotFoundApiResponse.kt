package com.example.kotlindockertest.configuration.annotation

import com.example.kotlindockertest.model.StringErrorResponse
import com.example.kotlindockertest.utils.NOT_FOUND_STATUS_CODE
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse

@ApiResponse(
    responseCode = NOT_FOUND_STATUS_CODE,
    content = [
        Content(
            schema = Schema(implementation = StringErrorResponse::class),
        ),
    ],
)
@Repeatable
annotation class NotFoundApiResponse(val description: String)
