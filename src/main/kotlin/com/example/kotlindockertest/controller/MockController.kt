package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.service.MockService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/mocks")
class MockController(private val mockService: MockService) {

    @GetMapping("/{id}")
    fun getMock(@PathVariable id: Long): MockDto {
        return mockService.getMock(id)
    }

    @PostMapping("/")
    fun addMock(@RequestBody mock: MockDto): StringIdResponse {
        val createdMockId = mockService.addMock(mock)

        return StringIdResponse(createdMockId)
    }

    @PutMapping("/{id}")
    fun patchMock(
        @PathVariable id: Long,
        @RequestBody mock: MockDto,
    ): MockDto {
        return mockService.patchMock(id, mock)
    }

    @DeleteMapping("/{id}")
    fun deleteMock(@PathVariable id: Long): Long {
        mockService.deleteMock(id)

        return id
    }
}
