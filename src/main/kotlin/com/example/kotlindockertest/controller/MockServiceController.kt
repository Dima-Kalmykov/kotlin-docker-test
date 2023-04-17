package com.example.kotlindockertest.controller

import com.example.kotlindockertest.configuration.annotation.NotFoundApiResponse
import com.example.kotlindockertest.configuration.annotation.SuccessfulApiResponse
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.service.MockServiceRequestDto
import com.example.kotlindockertest.service.MockService
import com.example.kotlindockertest.service.MockServiceHandler
import com.example.kotlindockertest.utils.SERVICE_NOT_FOUND_DESCRIPTION
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/graphql/services")
class MockServiceController(
    private val mockServiceHandler: MockServiceHandler,
    private val mockService: MockService,
) {

    @GetMapping
    fun getServices(@RequestParam(defaultValue = "") search: String) = mockServiceHandler.getServices(search)

    // Todo кажется, что не нужен, есть /getService
    @GetMapping("/{id}/mocks")
    fun getMocks(@PathVariable id: Long) = mockService.getMocks(id)

    @DeleteMapping("/{id}/mocks")
    fun deleteMocks(@PathVariable id: Long) = mockService.deleteMocks(id)

    @GetMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = SERVICE_NOT_FOUND_DESCRIPTION)
    fun getService(@PathVariable id: Long) = mockServiceHandler.getService(id)

    @PostMapping
    fun addService(
        @RequestAttribute username: String,
        @RequestBody service: MockServiceRequestDto,
        ): StringIdResponse {
        val createdServiceId = mockServiceHandler.addService(service, username)

        return StringIdResponse(createdServiceId)
    }

    @PutMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = SERVICE_NOT_FOUND_DESCRIPTION)
    fun patchService(
        @PathVariable id: Long,
        @RequestAttribute username: String,
        @RequestBody service: MockServiceRequestDto,
    ) = mockServiceHandler.patchService(id, service, username)

    @DeleteMapping("/{id}")
    fun deleteService(@PathVariable id: Long, @RequestAttribute username: String): StringIdResponse {
        mockServiceHandler.deleteService(id, username)

        return StringIdResponse(id)
    }
}
