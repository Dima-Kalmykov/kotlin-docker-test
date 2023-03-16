package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.exception.ServiceNotFoundException
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.service.MockServiceShortInfoDto
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
    fun getServices(search: String): List<MockServiceShortInfoDto> {
        val services = mockServiceRepository.getServices(search)
        services.forEach { service ->
            service.mocksCount = mockRepository.countByServiceId(service.id)
        }

        return services
    }

    @Transactional(readOnly = true)
    fun getServiceByName(name: String): MockServiceDto = mockServiceRepository.findByName(name)
        .orElseThrow { throw NotFoundException("Service with name $name doesn't exist") }

    // Todo ���������� ����� join
    @Transactional(readOnly = true)
    fun getService(id: Long): MockServiceDto = mockServiceRepository.findById(id).orElseThrow {
        throw ServiceNotFoundException(id)
    }.apply {
        this.mocks = mockRepository.getAllByServiceId(id)
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
            this.expirationDate = service.expirationDate
            this.location = service.location
            this.makeRealCall = service.makeRealCall
            this.delay = service.delay
            this.schema = service.schema
        }

        return updatedService
    }

    @Transactional
    fun deleteService(id: Long) {
        mockRepository.deleteMocksByServiceId(id)
        mockServiceRepository.deleteById(id)
    }
}
