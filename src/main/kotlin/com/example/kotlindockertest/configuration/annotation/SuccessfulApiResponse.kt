package com.example.kotlindockertest.configuration.annotation

import com.example.kotlindockertest.utils.SUCCESSFUL_STATUS_CODE
import io.swagger.v3.oas.annotations.responses.ApiResponse

@ApiResponse(responseCode = SUCCESSFUL_STATUS_CODE)
annotation class SuccessfulApiResponse
