package com.example.kotlindockertest.configuration

import graphql.parser.Parser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class DocumentConfiguration {

    @Bean
    fun documentParser() = Parser()
}
