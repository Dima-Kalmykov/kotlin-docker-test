package com.example.kotlindockertest.service.mock

import com.example.kotlindockertest.service.random.RandomGeneratorService
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service


private const val RANDOM_NUMBER = "#randomNumber"
private const val RANDOM_STRING = "#randomString"
private const val RANDOM_STR_NUMBER = "#randomStrNumber"

@Service
class MockRandomFieldsValidator(
    private val randomGeneratorService: RandomGeneratorService,
    private val objectMapper: ObjectMapper,
) {

    fun validateRandomFields(json: String): JsonNode {
        var currentJson = json
        var response = json
        var offset = 0

        while (currentJson.indexOf(RANDOM_NUMBER) != -1) {
            val randomNumberIndex = currentJson.indexOf(RANDOM_NUMBER)
            val randomNumberWordIndex = randomNumberIndex + RANDOM_NUMBER.length
            if (currentJson[randomNumberWordIndex] != '(') {
                error("Invalid format")
            }

            val jsonRightParenthesis = currentJson.drop(randomNumberWordIndex).indexOf(')')
            if (jsonRightParenthesis == -1) {
                error("Invalid format")
            }

            val lastRandomNumberIndex = jsonRightParenthesis + randomNumberWordIndex + 1
            val result = currentJson.substring(randomNumberWordIndex, lastRandomNumberIndex)
            val left = randomNumberIndex + offset
            val right = lastRandomNumberIndex + offset
            val randomValue = getRandomNumber(result)
            response = response.replaceFirst("\"${json.substring(left, right)}\"", randomValue.toString())

            currentJson = currentJson.drop(lastRandomNumberIndex)
            offset += lastRandomNumberIndex
        }

        val afterFirst = response
        currentJson = response
        offset = 0

        while (currentJson.indexOf(RANDOM_STRING) != -1) {
            val randomNumberIndex = currentJson.indexOf(RANDOM_STRING)
            val randomNumberWordIndex = randomNumberIndex + RANDOM_STRING.length
            if (currentJson[randomNumberWordIndex] != '(') {
                error("Invalid format")
            }

            val jsonRightParenthesis = currentJson.drop(randomNumberWordIndex).indexOf(')')
            if (jsonRightParenthesis == -1) {
                error("Invalid format")
            }

            val lastRandomNumberIndex = jsonRightParenthesis + randomNumberWordIndex + 1
            val result = currentJson.substring(randomNumberWordIndex, lastRandomNumberIndex)
            val left = randomNumberIndex + offset
            val right = lastRandomNumberIndex + offset
            val randomValue = getRandomString(result)
            response = response.replaceFirst(afterFirst.substring(left, right), randomValue)

            currentJson = currentJson.drop(lastRandomNumberIndex)
            offset += lastRandomNumberIndex
        }

        val afterSecond = response
        currentJson = response
        offset = 0

        while (currentJson.indexOf(RANDOM_STR_NUMBER) != -1) {
            val randomNumberIndex = currentJson.indexOf(RANDOM_STR_NUMBER)
            val randomNumberWordIndex = randomNumberIndex + RANDOM_STR_NUMBER.length
            if (currentJson[randomNumberWordIndex] != '(') {
                error("Invalid format")
            }

            val jsonRightParenthesis = currentJson.drop(randomNumberWordIndex).indexOf(')')
            if (jsonRightParenthesis == -1) {
                error("Invalid format")
            }

            val lastRandomNumberIndex = jsonRightParenthesis + randomNumberWordIndex + 1
            val result = currentJson.substring(randomNumberWordIndex, lastRandomNumberIndex)
            val left = randomNumberIndex + offset
            val right = lastRandomNumberIndex + offset
            val randomValue = getRandomNumber(result)
            response = response.replaceFirst(afterSecond.substring(left, right), randomValue.toString())

            currentJson = currentJson.drop(lastRandomNumberIndex)
            offset += lastRandomNumberIndex
        }

        return objectMapper.readTree(response)
    }

    private fun getRandomNumber(result: String) = onErrorThrow {
        val (leftValue, rightValue) = getBounds(result)

        randomGeneratorService.getLong(leftValue.toLong(), rightValue.toLong())
    }

    private fun getRandomString(result: String) = onErrorThrow {
        val (leftValue, rightValue) = getBounds(result)

        randomGeneratorService.getString(leftValue.toInt(), rightValue.toInt())
    }

    private fun getBounds(result: String): Pair<String, String> {
        val split = result.split(",")
        val leftValue = split[0].drop(1)
        val rightValue = split[1].drop(1).trimEnd(')')

        return leftValue to rightValue
    }

    private inline fun <T> onErrorThrow(func: () -> T) = try {
        func()
    } catch (exception: RuntimeException) {
        error("Invalid format. Details: ${exception.message}")
    }
}
