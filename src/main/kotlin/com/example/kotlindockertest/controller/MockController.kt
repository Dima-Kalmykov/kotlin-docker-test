package com.example.kotlindockertest.controller

import com.example.kotlindockertest.configuration.annotation.NotFoundApiResponse
import com.example.kotlindockertest.configuration.annotation.SuccessfulApiResponse
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.mock.MockDto
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
    @PostMapping("/")
    fun addMock(@RequestBody mock: MockDto): StringIdResponse {
        val createdMockId = mockService.addMock(mock)

        return StringIdResponse(createdMockId)
    }

    @PutMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = MOCK_NOT_FOUND_DESCRIPTION)
    fun patchMock(@PathVariable id: Long, @RequestBody mock: MockDto) =
        mockService.patchMock(id, mock)

    @DeleteMapping("/{id}")
    fun deleteMock(@PathVariable id: Long): StringIdResponse {
        mockService.deleteMock(id)

        return StringIdResponse(id)
    }
}
