package com.example.kotlindockertest.controller

import com.example.kotlindockertest.configuration.annotation.MockNotFoundApiResponse
import com.example.kotlindockertest.configuration.annotation.SuccessfulApiResponse
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.service.MockService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mocks")
class MockController(private val mockService: MockService) {

    @GetMapping("/{id}")
    @SuccessfulApiResponse
    @MockNotFoundApiResponse
    fun getMock(@PathVariable id: Long) = mockService.getMock(id)

    @PostMapping("/")
    fun addMock(@RequestBody mock: MockDto): StringIdResponse {
        val createdMockId = mockService.addMock(mock)

        return StringIdResponse(createdMockId)
    }

    @PutMapping("/{id}")
    @SuccessfulApiResponse
    @MockNotFoundApiResponse
    fun patchMock(@PathVariable id: Long, @RequestBody mock: MockDto) =
        mockService.patchMock(id, mock)

    @DeleteMapping("/{id}")
    fun deleteMock(@PathVariable id: Long): StringIdResponse {
        mockService.deleteMock(id)

        return StringIdResponse(id)
    }
}
