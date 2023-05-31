package com.example.kotlindockertest.service.random

import org.springframework.stereotype.Service

private val CHAR_POOL = ('a'..'z') + ('A'..'Z') + ('0'..'9')

@Service
class RandomGeneratorService {

    fun getLong(min: Long, max: Long) = (min..max).random()

    fun getString(minLength: Int, maxLength: Int): String {
        val length = (minLength..maxLength).random()
        val randomCharArray = List(length) { CHAR_POOL.random() }

        return randomCharArray.joinToString("")
    }
}
