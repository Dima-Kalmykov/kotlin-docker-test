package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.MockNotFoundException
import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.exception.ServiceNotFoundException
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.MockServiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MockServiceHandler(
    private val mockServiceRepository: MockServiceRepository,
    private val mockRepository: MockRepository,
) {

    @Transactional(readOnly = true)
    fun getServices() = mockServiceRepository.getServices()

    @Transactional(readOnly = true)
    fun getDefaultMock(id: Long): MockDto {
        val service = mockServiceRepository.findById(id).orElseThrow {
            throw ServiceNotFoundException(id)
        }

        val mockId = service.defaultMockId ?: error("Service with id = $id doesn't contain default mock")

        return mockRepository.findById(mockId).orElseThrow {
            throw MockNotFoundException(mockId)
        }
    }

    @Transactional(readOnly = true)
    fun getServiceByName(name: String): MockServiceDto = mockServiceRepository.findByName(name)
        .orElseThrow { throw NotFoundException("Service with name $name doesn't exist") }

    @Transactional(readOnly = true)
    fun getService(id: Long): MockServiceDto = mockServiceRepository.findById(id).orElseThrow {
        throw ServiceNotFoundException(id)
    }

    @Transactional
    fun addService(service: MockServiceDto): Long {
        val savedService = mockServiceRepository.save(service)

        return savedService.id
    }

    @Transactional
    fun patchService(id: Long, service: MockServiceDto): MockServiceDto {
        val updatedService = getService(id).apply {
            this.name = service.name
            this.host = service.host
            this.ttl = service.ttl
            this.location = service.location
            this.makeRealCall = service.makeRealCall
            this.defaultMockId = service.defaultMockId
            this.defaultMockId = service.delay
        }

        return updatedService
    }

    @Transactional
    fun deleteService(id: Long) {
        mockRepository.deleteMocksByServiceId(id)
        mockServiceRepository.deleteById(id)
    }
}
