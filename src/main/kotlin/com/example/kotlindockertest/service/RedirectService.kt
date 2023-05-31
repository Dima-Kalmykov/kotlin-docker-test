package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.RedirectException
import com.example.kotlindockertest.model.service.MockServiceDto
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder.fromHttpUrl

@Service
class RedirectService {

    fun callRealService(service: MockServiceDto, body: String): JsonNode? {
        val restTemplate = RestTemplate() // Todo вынести в конфигурацию

        val headers = HttpHeaders().apply {
            set("redirect", true.toString())
        }

        return try {
            val json = restTemplate.postForObject(
                buildUrl(service),
                HttpEntity(body, headers),
                JsonNode::class.java,
            )
            if (json?.get("errors") != null) {
                error("redirect response with error: $json")
            }

            json
        } catch (exception: RuntimeException) {
            throw RedirectException(exception.message!!)
        }
    }

    private fun buildUrl(service: MockServiceDto) = fromHttpUrl(service.location)
        .encode()
        .toUriString()
}
