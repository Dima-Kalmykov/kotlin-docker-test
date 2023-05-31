package com.example.kotlindockertest.configuration.web

import com.example.kotlindockertest.controller.interceptor.AuthorizationInterceptor
import com.example.kotlindockertest.controller.interceptor.LoggingInterceptor
import com.example.kotlindockertest.controller.interceptor.RedirectInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = false)
class InterceptorConfiguration : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        with(registry) {
            addInterceptor(LoggingInterceptor())
            addInterceptor(AuthorizationInterceptor())
            addInterceptor(RedirectInterceptor())
        }
    }
}
