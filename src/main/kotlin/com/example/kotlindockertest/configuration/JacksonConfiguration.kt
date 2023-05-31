package com.example.kotlindockertest.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

private const val DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss"

@Configuration(proxyBeanMethods = false)
class JacksonConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .findAndRegisterModules()
        .setDateFormat(SimpleDateFormat(DATE_TIME_PATTERN))
        .setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT")))
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}
