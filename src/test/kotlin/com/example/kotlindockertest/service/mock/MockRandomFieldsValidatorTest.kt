package com.example.kotlindockertest.service.mock

import com.example.kotlindockertest.service.random.RandomGeneratorService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test


class MockRandomFieldsValidatorTest {

    private val randomGeneratorService = RandomGeneratorService()
    private val mapper = ObjectMapper()
    private val mockRandom = MockRandomFieldsValidator(randomGeneratorService, mapper)
    private val randomNumber = "#randomNumber"

    private val json = """
        {
            "field1": "#randomNumber(1, 5)",
            "field2": "#randomString(1, 5)",
            "field3": "#randomStrNumber(20, 30)",
            "field4": "abdcs",
            "field5": "#randomNumber(20, 30)",
            "field6": "#randomString(36, 45)",
            "field6": 123
        }
    """.trimIndent()

    @Test
    fun testValidator() {
        val response = mockRandom.validateRandomFields(json)
        println(response.toPrettyString())
    }

    @Test
    fun test() {
        println("Json string = $json")
        var currentJson = json
        var stop = false
        var i = 25
        while (!stop) {
            val randomNumberIndex = currentJson.indexOf(randomNumber)
            if (randomNumberIndex == -1) {
                stop = true
            } else {
                val randomNumberWordIndex = randomNumberIndex + randomNumber.length
                if (currentJson[randomNumberWordIndex] != '(') {
                    error("Invalid format")
                }

                val jsonRightParenthesis = currentJson.drop(randomNumberWordIndex).indexOf(')')
                if (jsonRightParenthesis == -1) {
                    error("Invalid format")
                }

                val lastRandomNumberIndex = jsonRightParenthesis + randomNumberWordIndex + 1
                val result = currentJson.substring(randomNumberIndex + randomNumber.length, lastRandomNumberIndex)
                val randomValue = getRandomValue(result)

                currentJson = currentJson.drop(lastRandomNumberIndex)
            }
        }
    }

    private fun getRandomValue(result: String): Long {
        try {
            val split = result.split(",")
            val leftValue = split[0].drop(1).toLong()
            val rightValue = split[1].drop(1).trimEnd(')').toLong()

            return randomGeneratorService.getLong(leftValue, rightValue)
        } catch (exception: RuntimeException) {
            error("Invalid format. Details: ${exception.message}")
        }
    }
}
