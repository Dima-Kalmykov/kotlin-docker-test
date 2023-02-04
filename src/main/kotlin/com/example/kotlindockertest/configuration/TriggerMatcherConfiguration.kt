package com.example.kotlindockertest.configuration

import com.example.kotlindockertest.service.trigger.matcher.TriggerMatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class TriggerMatcherConfiguration {

    @Bean
    fun triggerMatchers(triggerMatchersList: List<TriggerMatcher>) = triggerMatchersList
        .groupBy { it.valueType }
        .mapValues {
            val matcher = it.value.first()
            if (it.value.size != 1) {
                error("Invalid number of trigger matchers with type ${matcher.valueType}")
            }

            matcher
        }
}
