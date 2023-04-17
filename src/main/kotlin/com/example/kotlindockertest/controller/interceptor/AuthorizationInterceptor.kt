package com.example.kotlindockertest.controller.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationInterceptor : HandlerInterceptor {

    private val decoder = Base64.getUrlDecoder()
    private val mapper = ObjectMapper()

    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorizationHeader = request.getHeader("Authorization") ?: return true

        val token = authorizationHeader.drop(7)
        val payloadChunk = token.split(".")[1]
        val payload = String(decoder.decode(payloadChunk))

        val jsonNode = mapper.readTree(payload)
        val username = jsonNode.get("user").toString().trim('\"')

        logger.info { "Username = $username" }

        request.setAttribute("username", username)
        return true
    }
}
