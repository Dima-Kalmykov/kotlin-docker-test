package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.TriggerNotFoundException
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.repository.TriggerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TriggerService(private val triggerRepository: TriggerRepository) {

    @Transactional(readOnly = true)
    fun getTriggers(mockId: Long): List<TriggerDto> {
        return triggerRepository.getTriggers(mockId)
    }

    @Transactional(readOnly = true)
    fun getTrigger(id: Long): TriggerDto {
        return triggerRepository.findById(id).orElseThrow {
            throw TriggerNotFoundException(id)
        }
    }

    @Transactional
    fun addTrigger(trigger: TriggerDto): Long {
        val savedTrigger = triggerRepository.save(trigger)

        return savedTrigger.id
    }

    @Transactional
    fun patchTrigger(id: Long, trigger: TriggerDto): TriggerDto {
        val updatedTrigger = getTrigger(id).apply {
            this.path = trigger.path
        }

        return updatedTrigger
    }

    @Transactional
    fun deleteTrigger(id: Long) {
        triggerRepository.deleteById(id)
    }
}
