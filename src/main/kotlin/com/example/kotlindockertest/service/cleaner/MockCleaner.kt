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

    @Transactional
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 5)
    fun removeMocksByTtl() {
        logger.info { "Start cleaning..." }
        mockRepository.deleteMocksByTtl()
    }
}
