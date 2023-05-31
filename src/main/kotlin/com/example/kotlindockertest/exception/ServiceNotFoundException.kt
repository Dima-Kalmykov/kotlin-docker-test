package com.example.kotlindockertest.exception

class ServiceNotFoundException(val id: Long) : NotFoundException(
    "Service with id = $id doesn't not exist",
)
