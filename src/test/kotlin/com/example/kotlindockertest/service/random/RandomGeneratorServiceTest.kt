package com.example.kotlindockertest.service.random

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.RepeatedTest


class RandomGeneratorServiceTest {

    private val randomGeneratorService = RandomGeneratorService()

    @RepeatedTest(value = 1000)
    fun `test getLong method`() {
        val leftBound = 1
        val middleBound = (100..250).random()
        val rightBound = 501

        val min = (leftBound..middleBound).random().toLong()
        val max = (middleBound + 1..rightBound).random().toLong()

        val randomValue = randomGeneratorService.getLong(min, max)

        assertThat(randomValue)
            .isLessThanOrEqualTo(max)
            .isGreaterThanOrEqualTo(min)
    }

    @RepeatedTest(value = 1000)
    fun `test getString method`() {
        val leftBound = 1
        val middleBound = (100..250).random()
        val rightBound = 501

        val min = (leftBound..middleBound).random()
        val max = (middleBound + 1..rightBound).random()

        val randomValue = randomGeneratorService.getString(min, max)

        assertThat(randomValue)
            .isAlphanumeric()
            .hasSizeBetween(min, max)
    }
}
