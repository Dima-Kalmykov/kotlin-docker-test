package com.example.kotlindockertest.controller.interceptor

import mu.KLogging
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RedirectInterceptor : HandlerInterceptor {

    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val redirectHeader = request.getHeader("redirect")
        if (redirectHeader.equals("true", true)) {
            error("Got request with header redirect: true")
        }

        return true
    }
}