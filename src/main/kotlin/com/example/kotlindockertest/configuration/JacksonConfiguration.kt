package com.example.kotlindockertest.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class JacksonConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().findAndRegisterModules()
}
