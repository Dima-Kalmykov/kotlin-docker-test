package com.example.kotlindockertest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinDockerTestApplication

fun main(args: Array<String>) {
    runApplication<KotlinDockerTestApplication>(*args)
}
