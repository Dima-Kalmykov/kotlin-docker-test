package com.example.kotlindockertest.exception

class MockNotFoundException(val id: Long) : NotFoundException(
    "Mock with id = $id doesn't not exist",
)
