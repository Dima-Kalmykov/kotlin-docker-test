package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.service.MockServiceDto
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder.fromHttpUrl

@Service
class RedirectService {

    fun callRealService(service: MockServiceDto, body: String): JsonNode? {
        val restTemplate = RestTemplate() // Todo вынести в конфигурацию

        return restTemplate.postForObject(
            buildUrl(service),
            body,
            JsonNode::class.java,
        )
    }

    private fun buildUrl(service: MockServiceDto) = fromHttpUrl(service.location)
        .encode()
        .toUriString()
}
