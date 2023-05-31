package com.example.kotlindockertest

import org.junit.jupiter.api.Test
import java.time.ZonedDateTime


class TestTabs {

    @Test
    fun `test tabs`() {
        val tab = "\t"
        println("Tab value = [$tab]")
        println(ZonedDateTime.now().toString())
    }
}