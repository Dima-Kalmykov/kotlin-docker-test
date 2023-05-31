package com.example.kotlindockertest.service.cleaner

import com.example.kotlindockertest.repository.MockRepository
import com.example.kotlindockertest.repository.MockServiceRepository
import com.example.kotlindockertest.repository.TriggerRepository
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class MockServiceCleaner(
    private val mockServiceRepository: MockServiceRepository,
    private val triggerRepository: TriggerRepository,
    private val mockRepository: MockRepository,
) {

    companion object : KLogging()

    @Transactional
    @Scheduled(timeUnit = TimeUnit.HOURS, fixedDelay = 1)
    fun removeServicesByTtl() {
        logger.info { "Start cleaning services..." }
        val servicesToBeDeleted = mockServiceRepository.getServicesToBeDeleted()
        servicesToBeDeleted.forEach { service ->
            val mocks = mockRepository.getAllByServiceId(service.id)
            mocks.forEach {  mock ->
                triggerRepository.deleteTriggersByMockId(mock.id!!)
            }
            mockRepository.deleteMocksByServiceId(service.id)
        }

        mockServiceRepository.deleteServicesByTtl()
        logger.info { "Finish cleaning services" }
    }
}
