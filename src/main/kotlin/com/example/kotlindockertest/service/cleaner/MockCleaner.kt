package com.example.kotlindockertest.service.cleaner

import com.example.kotlindockertest.repository.MockRepository
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class MockCleaner(private val mockRepository: MockRepository) {

    companion object : KLogging()

    // Todo сделать для сервисов
//    @Transactional
//    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 5)
    fun removeMocksByTtl() {
        // Todo mark as deleted
        logger.info { "Start cleaning mocks..." }
        mockRepository.deleteMocksByTtl()
        logger.info { "Finish cleaning mocks" }
    }
}
