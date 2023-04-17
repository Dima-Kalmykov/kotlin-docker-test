package com.example.kotlindockertest.service

import com.example.kotlindockertest.exception.AuthorizationException
import com.example.kotlindockertest.exception.TriggerNotFoundException
import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.repository.TriggerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TriggerService(
    private val triggerRepository: TriggerRepository,
    private val triggerPathTokenizer: TriggerPathTokenizer,
) {

    @Transactional(readOnly = true)
    fun getTriggers(mockId: Long): List<TriggerDto> {
        return triggerRepository.getTriggers(mockId)
    }

    @Transactional(readOnly = true)
    fun getTriggersByServiceId(serviceId: Long): List<TriggerDto> {
        return triggerRepository.findAllByServiceId(serviceId)
    }

    @Transactional(readOnly = true)
    fun getTrigger(id: Long): TriggerDto {
        return triggerRepository.findById(id).orElseThrow {
            throw TriggerNotFoundException(id)
        }
    }

    @Transactional
    fun addTrigger(trigger: TriggerDto, username: String): Long {
        triggerPathTokenizer.tokenize(trigger.path)
        trigger.createdBy = username
        val savedTrigger = triggerRepository.save(trigger)

        return savedTrigger.id
    }

    @Transactional
    fun activateTrigger(id: Long, request: ActivateRequestDto, username: String): TriggerDto {
        val updatedTrigger = getTrigger(id)
        if (updatedTrigger.createdBy != username) {
            throw AuthorizationException()
        }

        updatedTrigger.apply {
            this.enable = request.enable
        }

        return updatedTrigger
    }

    @Transactional
    fun patchTrigger(id: Long, trigger: TriggerDto, username: String): TriggerDto {
        val updatedTrigger = getTrigger(id)
        if (updatedTrigger.createdBy != username) {
            throw AuthorizationException()
        }
        triggerPathTokenizer.tokenize(trigger.path)

        updatedTrigger.apply {
            this.operation = trigger.operation
            this.valueType = trigger.valueType
            this.value = trigger.value
            this.enable = trigger.enable
            this.serviceId = trigger.serviceId
        }

        return updatedTrigger
    }

    @Transactional
    fun deleteTrigger(id: Long, username: String) {
        val trigger = getTrigger(id)
        if (trigger.createdBy != username) {
            throw AuthorizationException()
        }
        triggerRepository.deleteById(id)
    }
}
