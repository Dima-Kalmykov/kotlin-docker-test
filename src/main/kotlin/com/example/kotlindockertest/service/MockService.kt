package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.MockNotFoundException
import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.mock.MockRequestDto
import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.TriggerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class MockService(
    private val mockRepository: MockRepository,
    private val triggerRepository: TriggerRepository,
) {

    @Transactional(readOnly = true)
    fun getMocks(serviceId: Long) = mockRepository
        .getAllByServiceId(serviceId)
        .filter { !it.isExpired() }

    // Todo проверить, что делает один нужный join
    @Transactional(readOnly = true)
    fun getMockByRequestHash(serviceId: Long, requestHash: Int): MockDto? {
        val mock = mockRepository.findByServiceIdAndRequestHash(serviceId, requestHash)

        if (!mock.isPresent) {
            return null
        }

        return if (mock.get().isExpired()) {
            null
        } else {
            mock.get()
        }
    }

    // Todo sort by service id
    fun getMockByName(serviceId: Long, mockName: String): MockDto {
        val mock = mockRepository.findByServiceIdAndName(serviceId, mockName)

        if (!mock.isPresent) {
            throw MockNotFoundException(321) // Todo change id
        }

        if (mock.get().isExpired()) {
            throw MockNotFoundException(123) // Todo change id
        } else {
            return mock.get()
        }
    }

    @Transactional(readOnly = true)
    fun getMock(id: Long): MockDto {
        val mock = mockRepository.findById(id)

        if (!mock.isPresent) {
            throw MockNotFoundException(id)
        }

        if (mock.get().isExpired()) {
            throw MockNotFoundException(id)
        } else {
            return mock.get()
        }
    }

    @Transactional
    // Todo get mock and check is expirationDate, if expired then replace
    fun addMock(mock: MockRequestDto): Long {
        val mappedMock = MockDto(
            name = mock.name,
            expirationDate = mock.expirationDate,
            delay = mock.delay,
            enable = mock.enable,
            request = mock.request,
            requestHash = mock.request
                .replace("\r", "")
                .replace("\t", "    ")
                .replace("\\t", "    ")
                .replace("\\\"", "")
                .hashCode(),
            response = mock.response,
            serviceId = mock.serviceId,
        )

        val savedMock = mockRepository.save(mappedMock)

        return savedMock.id!!
    }

    @Transactional
    fun patchMock(id: Long, mock: MockRequestDto): MockDto {
        val updatedMock = getMock(id).apply {
            fillMockData(mock)
        }

        return updatedMock
    }

    @Transactional
    fun activateMock(id: Long, request: ActivateRequestDto): MockDto {
        val updatedMock = getMock(id).apply {
            this.enable = request.enable
        }

        return updatedMock
    }

    @Transactional
    fun deleteMock(id: Long) {
        mockRepository.deleteById(id)
        triggerRepository.deleteTriggersByMockId(id)
    }

    @Transactional
    fun deleteMocks(id: Long) {
        mockRepository.deleteMocksByServiceId(id)
        triggerRepository.deleteTriggersByServiceId(id)
    }

    private fun MockDto.fillMockData(mock: MockRequestDto) = apply {
        this.name = mock.name
        this.expirationDate = mock.expirationDate
        this.request = mock.request
        this.requestHash = mock.request
            .replace("\r", "")
            .replace("\t", "    ")
            .replace("\\t", "    ")
            .replace("\\\"", "")
            .hashCode()
        this.response = mock.response
        this.delay = mock.delay
        this.enable = mock.enable
    }

    private fun MockDto.isExpired() =
        expirationDate != null && expirationDate!! < ZonedDateTime.now()
}
