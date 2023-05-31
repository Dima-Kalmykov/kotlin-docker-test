package com.example.kotlindockertest.exception

class TriggerNotFoundException(val id: Long) : NotFoundException(
    "Trigger with id = $id doesn't not exist",
)
