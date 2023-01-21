package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.mock.MockShortInfoDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.service.MockService
import com.example.kotlindockertest.service.MockServiceHandler
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/services")
class MockServiceController(
    private val mockServiceHandler: MockServiceHandler,
    private val mockService: MockService,
) {

    @GetMapping("/")
    fun getServices() = mockServiceHandler.getServices()

    @GetMapping("/{id}/mocks")
    fun getMocks(
        @PathVariable id: Long,
    ): List<MockShortInfoDto> {
        return mockService.getMocks(id)
    }

    @GetMapping("/{id}/mocks/default")
    fun getDefaultMock(@PathVariable id: Long): MockDto {
        return mockServiceHandler.getDefaultMock(id)
    }

    @GetMapping("/{id}")
    fun getService(@PathVariable id: Long): MockServiceDto {
        return mockServiceHandler.getService(id)
    }

    @PostMapping("/")
    fun addService(@RequestBody service: MockServiceDto): StringIdResponse {
        val createdServiceId = mockServiceHandler.addService(service)

        return StringIdResponse(createdServiceId)
    }

    @PutMapping("/{id}")
    fun patchService(
        @PathVariable id: Long,
        @RequestBody service: MockServiceDto,
    ): MockServiceDto {
        return mockServiceHandler.patchService(id, service)
    }

    @DeleteMapping("/{id}")
    fun deleteService(@PathVariable id: Long): StringIdResponse {
        mockServiceHandler.deleteService(id)

        return StringIdResponse(id)
    }
}
