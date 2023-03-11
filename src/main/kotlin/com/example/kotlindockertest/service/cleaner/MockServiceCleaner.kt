package com.example.kotlindockertest.service.cleaner

import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.MockServiceRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class MockServiceCleaner(private val mockServiceRepository: MockServiceRepository) {

    companion object : KLogging()

    // Todo сделать для сервисов
//    @Transactional
//    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 5)
    fun removeMocksByTtl() {
        // Todo mark as deleted
        logger.info { "Start cleaning mocks..." }
        mockServiceRepository.deleteServicesByTtl()
        logger.info { "Finish cleaning mocks" }
    }
}