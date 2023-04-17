package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.AuthorizationException
import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.exception.ServiceNotFoundException
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.service.MockServiceRequestDto
import com.example.kotlindockertest.model.service.MockServiceShortInfoDto
import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.MockServiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class MockServiceHandler(
    private val mockServiceRepository: MockServiceRepository,
    private val mockRepository: MockRepository,
) {

    @Transactional(readOnly = true)
    fun getServices(search: String): List<MockServiceShortInfoDto> {
        val services = mockServiceRepository
            .getServices(search.uppercase())
            .filter { !it.isExpired() }

        services.forEach { service ->
            service.mocksCount = mockRepository.countByServiceId(service.id)
        }

        return services
    }

    @Transactional(readOnly = true)
    fun getServiceByName(name: String): MockServiceDto {
        val service = mockServiceRepository.findByName(name)

        if (!service.isPresent) {
            throw NotFoundException("Service with name $name doesn't exist")
        }

        if (service.get().isExpired()) {
            throw NotFoundException("Service with name $name doesn't exist")
        } else {
            return service.get()
        }
    }

    // Todo переделать через join
    @Transactional(readOnly = true)
    fun getService(id: Long): MockServiceDto {
        val service = mockServiceRepository.findById(id)

        if (!service.isPresent) {
            throw ServiceNotFoundException(id)
        }

        if (service.get().isExpired()) {
            throw ServiceNotFoundException(id)
        } else {
            return service.get().apply {
                this.mocks = mockRepository.getAllByServiceId(id)
            }
        }
    }

    @Transactional
    fun addService(service: MockServiceRequestDto, username: String): Long {
        val existingService = mockServiceRepository.findByName(service.name)

        if (!existingService.isPresent) {
            val mappedService = MockServiceDto(
                name = service.name,
                location = service.location,
                makeRealCall = service.makeRealCall,
                expirationDate = service.expirationDate,
                schema = service.schema,
                delay = service.delay,
                createdBy = username,
            )
            val savedService = mockServiceRepository.save(mappedService)

            return savedService.id!!
        }

        val updatableService = existingService.get()

        if (updatableService.isExpired()) {
            updatableService.apply {
                fillServiceData(service)
                this.createdBy = username
            }

            return updatableService.id!!
        }

        error("Service with name ${service.name} already exists")
    }

    @Transactional
    fun patchService(id: Long, service: MockServiceRequestDto, username: String): MockServiceDto {
        val updatedService = getService(id)
        if (updatedService.createdBy != username) {
            throw AuthorizationException()
        }
        updatedService.apply {
            fillServiceData(service)
        }

        return updatedService
    }

    @Transactional
    fun deleteService(id: Long, username: String) {
        val service = getService(id)
        if (service.createdBy != username) {
            throw AuthorizationException()
        }
        mockRepository.deleteMocksByServiceId(id)
        mockServiceRepository.deleteById(id)
    }

    private fun MockServiceDto.fillServiceData(service: MockServiceRequestDto) = apply {
        this.name = service.name
        this.expirationDate = service.expirationDate
        this.location = service.location
        this.makeRealCall = service.makeRealCall
        this.delay = service.delay
        this.schema = service.schema
    }

    private fun MockServiceDto.isExpired() =
        expirationDate != null && expirationDate!! < ZonedDateTime.now()

    private fun MockServiceShortInfoDto.isExpired() =
        expirationDate != null && expirationDate!! < ZonedDateTime.now()
}
