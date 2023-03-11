package com.example.kotlindockertest.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

private const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm"

@Configuration(proxyBeanMethods = false)
class JacksonConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .findAndRegisterModules()
        .setDateFormat(SimpleDateFormat(DATE_TIME_PATTERN))
}
