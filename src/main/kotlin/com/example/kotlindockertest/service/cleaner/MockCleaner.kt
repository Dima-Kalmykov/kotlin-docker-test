package com.example.kotlindockertest.service.cleaner

import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.TriggerRepository
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class MockCleaner(
    private val triggerRepository: TriggerRepository,
    private val mockRepository: MockRepository,
) {

    companion object : KLogging()

    @Transactional
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 30)
    fun removeMocksByTtl() {
        logger.info { "Start cleaning mocks..." }
        val mocksToBeDeleted = mockRepository.getMocksToBeDeleted()
        mocksToBeDeleted.forEach { mock ->
            triggerRepository.deleteTriggersByMockId(mock.id)
        }

        mockRepository.deleteMocksByTtl()
        logger.info { "Finish cleaning mocks" }
    }
}
