package com.example.kotlindockertest.controller.interceptor

import mu.KLogging
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingInterceptor : HandlerInterceptor {

    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info {
            "\nNew request: ${request.method} ${request.requestURL}"
        }

        return true
    }
}
