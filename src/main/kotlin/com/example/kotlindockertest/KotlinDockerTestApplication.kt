package com.example.kotlindockertest

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class KotlinDockerTestApplication

fun main(args: Array<String>) {
    runApplication<KotlinDockerTestApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
