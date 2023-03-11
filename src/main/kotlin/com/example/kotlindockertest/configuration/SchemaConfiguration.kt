package com.example.kotlindockertest.configuration

import graphql.analysis.QueryVisitorStub
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class SchemaConfiguration {

    @Bean
    fun schemaGenerator() = SchemaGenerator()

    @Bean
    fun schemaParser() = SchemaParser()

    @Bean
    fun queryVisitor() = QueryVisitorStub()
}
