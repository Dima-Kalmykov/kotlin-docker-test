package com.example.kotlindockertest

import com.example.kotlindockertest.configuration.RedirectRestProperties
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RedirectRestProperties::class)
class KotlinDockerTestApplication

fun main(args: Array<String>) {
    runApplication<KotlinDockerTestApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
