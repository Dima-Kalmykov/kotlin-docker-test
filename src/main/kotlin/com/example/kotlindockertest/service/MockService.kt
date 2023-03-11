package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.MockNotFoundException
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.TriggerRepository
import com.example.kotlindockertest.utils.toDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class MockService(
    private val mockRepository: MockRepository,
    private val triggerRepository: TriggerRepository,
) {

    @Transactional(readOnly = true)
    fun getMocks(serviceId: Long) = mockRepository.getMocks(serviceId)

    // Todo проверить, что делает один нужный join
    @Transactional(readOnly = true)
    fun getMockByRequestHash(serviceId: Long, requestHash: Int): MockDto? =
        mockRepository.findByServiceIdAndRequestHash(serviceId, requestHash).orElse(null)

    // Todo sort by service id
    fun getMockByName(serviceId: Long, mockName: String): MockDto {
        return mockRepository.findByServiceIdAndName(serviceId, mockName).orElseThrow {
            error("") // todo
        }
    }

    @Transactional(readOnly = true)
    fun getMock(id: Long): MockDto = mockRepository.findById(id).orElseThrow {
        throw MockNotFoundException(id)
    }

    @Transactional
    fun addMock(mock: MockDto): Long {
        val savedMock = mockRepository.save(mock)

        return savedMock.id
    }

    @Transactional
    fun patchMock(id: Long, mock: MockDto): MockDto {
        val updatedMock = getMock(id).apply {
            this.name = mock.name
            this.expirationDate = mock.expirationDate
            this.request = mock.request
            this.response = mock.response
            this.delay = mock.delay
            this.enable = mock.enable
        }

        return updatedMock
    }

    @Transactional
    fun deleteMock(id: Long) {
        mockRepository.deleteById(id)
        triggerRepository.deleteTriggersByMockId(id)
    }
}
