package com.example.kotlindockertest.controller

import com.example.kotlindockertest.configuration.annotation.NotFoundApiResponse
import com.example.kotlindockertest.configuration.annotation.SuccessfulApiResponse
import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.mock.MockRequestDto
import com.example.kotlindockertest.service.MockService
import com.example.kotlindockertest.utils.MOCK_NOT_FOUND_DESCRIPTION
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/graphql/mocks")
class MockController(private val mockService: MockService) {

    @GetMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = MOCK_NOT_FOUND_DESCRIPTION)
    fun getMock(@PathVariable id: Long) = mockService.getMock(id)

    // Todo foreign key service id
    @PostMapping
    fun addMock(
        @RequestAttribute username: String,
        @RequestBody mock: MockRequestDto,
    ): StringIdResponse {
        val createdMockId = mockService.addMock(mock, username)

        return StringIdResponse(createdMockId)
    }

    @PatchMapping("/{id}")
    fun activateMock(
        @PathVariable id: Long,
        @RequestAttribute username: String,
        @RequestBody request: ActivateRequestDto,
    ) = mockService.activateMock(id, request, username)

    @PutMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = MOCK_NOT_FOUND_DESCRIPTION)
    fun patchMock(
        @PathVariable id: Long,
        @RequestAttribute username: String,
        @RequestBody mock: MockRequestDto,
        ) = mockService.patchMock(id, mock, username)

    @DeleteMapping("/{id}")
    fun deleteMock(@PathVariable id: Long, @RequestAttribute username: String): StringIdResponse {
        mockService.deleteMock(id, username)

        return StringIdResponse(id)
    }
}
