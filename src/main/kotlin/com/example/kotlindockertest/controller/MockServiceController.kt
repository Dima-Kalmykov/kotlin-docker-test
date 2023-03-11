package com.example.kotlindockertest.controller

import com.example.kotlindockertest.configuration.annotation.NotFoundApiResponse
import com.example.kotlindockertest.configuration.annotation.SuccessfulApiResponse
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.service.MockService
import com.example.kotlindockertest.service.MockServiceHandler
import com.example.kotlindockertest.utils.MOCK_NOT_FOUND_DESCRIPTION
import com.example.kotlindockertest.utils.SERVICE_NOT_FOUND_DESCRIPTION
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/graphql/services")
class MockServiceController(
    private val mockServiceHandler: MockServiceHandler,
    private val mockService: MockService,
) {

    @GetMapping("/")
    fun getServices() = mockServiceHandler.getServices()

    // Todo кажется, что не нужен, есть /getService
    @GetMapping("/{id}/mocks")
    fun getMocks(@PathVariable id: Long) = mockService.getMocks(id)

    @GetMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = SERVICE_NOT_FOUND_DESCRIPTION)
    fun getService(@PathVariable id: Long) = mockServiceHandler.getService(id)

    @PostMapping("/")
    fun addService(@RequestBody service: MockServiceDto): StringIdResponse {
        val createdServiceId = mockServiceHandler.addService(service)

        return StringIdResponse(createdServiceId)
    }

    @PutMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = SERVICE_NOT_FOUND_DESCRIPTION)
    fun patchService(
        @PathVariable id: Long,
        @RequestBody service: MockServiceDto,
    ) = mockServiceHandler.patchService(id, service)

    @DeleteMapping("/{id}")
    fun deleteService(@PathVariable id: Long): StringIdResponse {
        mockServiceHandler.deleteService(id)

        return StringIdResponse(id)
    }
}
