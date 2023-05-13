package com.example.kotlindockertest.controller.interceptor

import com.example.kotlindockertest.exception.JWTTokenException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationInterceptor : HandlerInterceptor {

    private val decoder = Base64.getUrlDecoder()
    private val mapper = ObjectMapper()

    companion object : KLogging()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val authorizationHeader = request.getHeader("Authorization")

        if (request.method == RequestMethod.HEAD.name) {
            return true
        }

//        request.setAttribute("username", "Dima")
//        return true

        if ("/mocker/" in request.requestURI ||
            "/error" in request.requestURI ||
            "/swagger-ui" in request.requestURI ||
            "/api-docs" in request.requestURI) {
            return true
        }

        if (authorizationHeader == null) {
            throw JWTTokenException("Authorization header is not presented for ${request.method} request")
        }

        return try {
            val token = authorizationHeader.drop(7)
            val payloadChunk = token.split(".")[1]
            val payload = String(decoder.decode(payloadChunk))

            val jsonNode = mapper.readTree(payload)
            val username = jsonNode.get("user").toString().trim('\"')

            logger.info { "Username = $username" }

            request.setAttribute("username", username)
            true
        } catch (ex: Exception) {
            println("Error: ${ex.message}")
            throw JWTTokenException("Invalid JWT token")
        }
    }
}
