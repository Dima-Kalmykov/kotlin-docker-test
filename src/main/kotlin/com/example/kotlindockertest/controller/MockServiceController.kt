package com.example.kotlindockertest.controller

import com.example.kotlindockertest.configuration.annotation.NotFoundApiResponse
import com.example.kotlindockertest.configuration.annotation.SuccessfulApiResponse
import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.service.MockServiceRequestDto
import com.example.kotlindockertest.service.MockServiceHandler
import com.example.kotlindockertest.service.history.HistoryService
import com.example.kotlindockertest.service.mock.MockService
import com.example.kotlindockertest.utils.SERVICE_NOT_FOUND_DESCRIPTION
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.random.RandomGenerator.StreamableGenerator

@RestController
@RequestMapping("/graphql/services")
class MockServiceController(
    private val mockServiceHandler: MockServiceHandler,
    private val mockService: MockService,
    private val historyService: HistoryService,
) {

    @RequestMapping("/{serviceName}", method = [RequestMethod.HEAD])
    fun getServiceByName(@PathVariable serviceName: String): ResponseEntity<Any>? {
        val serviceIsExist = mockServiceHandler.isExist(serviceName)

        return if (serviceIsExist) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getServices(
        @RequestParam(defaultValue = "") search: String,
        @RequestAttribute username: String,
    ) = mockServiceHandler.getServices(search, username)

    // Todo кажется, что не нужен, есть /getService
    @GetMapping("/{id}/mocks")
    fun getMocks(
        @PathVariable id: Long,
        @RequestAttribute username: String,
    ) = mockService.getMocks(id, username)

    // Todo нет авторизации
    @DeleteMapping("/{id}/mocks")
    fun deleteMocks(
        @PathVariable id: Long,
        @RequestAttribute username: String,
    ) = mockService.deleteMocks(id, username)

    @PatchMapping("/{id}/history")
    fun activateHistory(
        @PathVariable id: Long,
        @RequestAttribute username: String,
        @RequestBody request: ActivateRequestDto,
    ): MockServiceDto {
        return historyService.activateHistory(id, request, username)
    }

    @GetMapping("/{id}")
    @SuccessfulApiResponse
    @NotFoundApiResponse(description = SERVICE_NOT_FOUND_DESCRIPTION)
    fun getService(
        @PathVariable id: Long,
        @RequestAttribute username: String,
    ) = mockServiceHandler.getService(id, username)

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
