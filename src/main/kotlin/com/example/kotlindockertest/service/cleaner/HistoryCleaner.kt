package com.example.kotlindockertest.service.cleaner

import com.example.kotlindockertest.repository.HistoryRepository
import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class HistoryCleaner(private val historyRepository: HistoryRepository) {

    companion object : KLogging()

    @Transactional
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 15)
    fun removeHistoryEventsByTtl() {
        logger.info { "Start cleaning history events..." }

        historyRepository.removeHistoryEventsByTtl()

        logger.info { "Finish cleaning history events" }
    }
}
