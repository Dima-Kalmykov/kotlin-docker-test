package com.example.kotlindockertest.model.service

import java.time.ZonedDateTime

class MockServiceShortInfoDto(
    var id: Long,
    var name: String,
    var location: String,
    var createdBy: String,
    var expirationDate: ZonedDateTime? = null,
) {
    var mocksCount: Long = 0
}
