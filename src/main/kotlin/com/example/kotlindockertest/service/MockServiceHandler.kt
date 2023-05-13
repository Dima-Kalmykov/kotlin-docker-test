package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.AuthorizationException
import com.example.kotlindockertest.exception.NotFoundException
import com.example.kotlindockertest.exception.ServiceNotFoundException
import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.service.MockServiceRequestDto
import com.example.kotlindockertest.model.service.MockServiceShortInfoDto
import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.MockServiceRepository
import com.example.kotlindockertest.repository.TriggerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class MockServiceHandler(
    private val mockServiceRepository: MockServiceRepository,
    private val mockRepository: MockRepository,
    private val triggerRepository: TriggerRepository,
) {

    @Transactional(readOnly = true)
    fun isExist(serviceName: String): Boolean {
        return mockServiceRepository.findByName(serviceName).isPresent
    }

    @Transactional(readOnly = true)
    fun getServices(search: String, username: String): List<MockServiceShortInfoDto> {
        val services = mockServiceRepository
            .getServices(search.uppercase())
            .filter { !it.isExpired() }
            .filter { it.createdBy == username }
            .sortedBy { it.id }

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
    fun getService(id: Long, username: String): MockServiceDto {
        val service = mockServiceRepository.findById(id)

        if (!service.isPresent) {
            throw ServiceNotFoundException(id)
        }

        return when {
            service.get().isExpired() -> throw ServiceNotFoundException(id)
            service.get().createdBy != username -> throw AuthorizationException()

            else -> service.get().apply {
                this.mocks = mockRepository.getAllByServiceId(id)
            }
        }
    }

    @Transactional
    fun addService(service: MockServiceRequestDto, username: String): Long {
        val existingService = mockServiceRepository.findByName(service.name)

        if (service.historyStorageDuration > 1440 * 60) {
            error("History can't be stored for more than 60 days")
        }

        if (!existingService.isPresent) {
            val mappedService = MockServiceDto(
                name = service.name,
                location = service.location,
                makeRealCall = service.makeRealCall,
                expirationDate = service.expirationDate,
                schema = service.schema,
                delay = service.delay,
                createdBy = username,
                historyStorageDuration = service.historyStorageDuration,
                storeHistory = service.storeHistory,
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
        val updatedService = getService(id, username)
        if (service.historyStorageDuration > 1440 * 60) {
            error("History can't be stored for more than 60 days")
        }
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
        val service = getService(id, username)
        if (service.createdBy != username) {
            throw AuthorizationException()
        }
        val mocksToBeDeleted = mockRepository.getAllByServiceId(id)
        mocksToBeDeleted.forEach {
            triggerRepository.deleteTriggersByMockId(it.id!!)
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
        this.historyStorageDuration = service.historyStorageDuration
        this.storeHistory = service.storeHistory
    }

    private fun MockServiceDto.isExpired() =
        expirationDate != null && expirationDate!! < ZonedDateTime.now()

    private fun MockServiceShortInfoDto.isExpired() =
        expirationDate != null && expirationDate!! < ZonedDateTime.now()
}
