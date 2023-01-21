package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.MockNotFoundException
import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.repository.MockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MockService(private val mockRepository: MockRepository) {

    @Transactional(readOnly = true)
    fun getMocks(serviceId: Long) = mockRepository.getMocks(serviceId)


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
            this.ttl = mock.ttl
            this.request = mock.request
            this.response = mock.response
            this.delay = mock.delay
        }

        return updatedMock
    }

    @Transactional
    fun deleteMock(id: Long) {
        mockRepository.deleteById(id)
    }
}
