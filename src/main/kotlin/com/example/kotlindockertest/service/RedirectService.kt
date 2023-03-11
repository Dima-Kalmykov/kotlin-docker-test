package com.example.kotlindockertest.service

import com.example.kotlindockertest.model.service.MockServiceDto
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder.fromHttpUrl

@Service
class RedirectService {

    fun callRealService(service: MockServiceDto, body: JsonNode): JsonNode? {
        val restTemplate = RestTemplate() // Todo вынести в конфигурацию

        return restTemplate.getForObject(
            buildUrl(service),
            JsonNode::class.java,
            body,
        )
    }

    private fun buildUrl(service: MockServiceDto): String {
        return fromHttpUrl(service.location)
            .query("query={body}")
            .encode()
            .toUriString()
    }
}
