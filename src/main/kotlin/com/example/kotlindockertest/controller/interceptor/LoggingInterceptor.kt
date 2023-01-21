package com.example.kotlindockertest.controller.interceptor

import mu.KLogging
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingInterceptor : HandlerInterceptor {

    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info {
            """
                Method: ${request.method}
                URL: ${request.requestURL}
            """.trimIndent()
        }

        return true
    }
}
