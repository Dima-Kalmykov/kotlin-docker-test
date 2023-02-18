package com.example.kotlindockertest.utils

import java.time.LocalDateTime

fun Long.toDateTime() = LocalDateTime.now().plusSeconds(this)
