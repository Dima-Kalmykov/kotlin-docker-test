package com.example.kotlindockertest.service.mock

import com.example.kotlindockertest.exception.AuthorizationException
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
    private val randomFieldsValidator: MockRandomFieldsValidator,
) {

    @Transactional(readOnly = true)
    fun getMocks(serviceId: Long, username: String, isInternalCall: Boolean = false): List<MockDto> {
        if (isInternalCall) {
            return mockRepository
                .getAllByServiceId(serviceId)
                .filter { !it.isExpired() }
                .sortedBy { it.id }
        }
        return mockRepository
            .getAllByServiceId(serviceId)
            .filter { !it.isExpired() }
            .filter { it.createdBy == username }
            .sortedBy { it.id }
    }

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

    @Transactional(readOnly = true)
    fun getMock(id: Long, username: String, isInternalCall: Boolean): MockDto {
        val mock = mockRepository.findById(id)

        if (!mock.isPresent) {
            throw MockNotFoundException(id)
        }

        return when {
            mock.get().isExpired() -> throw MockNotFoundException(id)
            !isInternalCall && mock.get().createdBy != username -> throw AuthorizationException()
            else -> mock.get()
        }
    }

    @Transactional
    fun addMock(mock: MockRequestDto, username: String): Long {
        randomFieldsValidator.validateRandomFields(mock.response)
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
            createdBy = username,
        )

        val savedMock = mockRepository.save(mappedMock)

        return savedMock.id!!
    }

    @Transactional
    fun patchMock(id: Long, mock: MockRequestDto, username: String): MockDto {
        randomFieldsValidator.validateRandomFields(mock.response)
        val updatedMock = getMock(id, username, true)
        if (updatedMock.createdBy != username) {
            throw AuthorizationException()
        }

        updatedMock.fillMockData(mock)

        return updatedMock
    }

    @Transactional
    fun activateMock(id: Long, request: ActivateRequestDto, username: String): MockDto {
        val updatedMock = getMock(id, username, true)
        if (updatedMock.createdBy != username) {
            throw AuthorizationException()
        }

        updatedMock.apply {
            this.enable = request.enable
        }

        return updatedMock
    }

    @Transactional
    fun deleteMock(id: Long, username: String) {
        val mock = getMock(id, username, true)
        if (mock.createdBy != username) {
            throw AuthorizationException()
        }

        mockRepository.deleteById(id)
        triggerRepository.deleteTriggersByMockId(id)
    }

    @Transactional
    fun deleteMocks(id: Long, username: String) {
        val mocks = getMocks(id, username)
        mocks.forEach {
            triggerRepository.deleteTriggersByMockId(it.id!!)
        }
        mockRepository.deleteMocksByServiceId(id)
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
