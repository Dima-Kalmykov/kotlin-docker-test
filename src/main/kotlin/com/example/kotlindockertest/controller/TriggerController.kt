package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.TriggerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/graphql")
class TriggerController(private val triggerService: TriggerService) {

    @GetMapping("/mocks/{mockId}/triggers")
    fun getTriggers(
        @PathVariable mockId: Long,
        @RequestAttribute username: String,
    ) = triggerService.getTriggers(mockId)

    @GetMapping("/triggers/{id}")
    fun getTrigger(
        @PathVariable id: Long,
        @RequestAttribute username: String,
    ) = triggerService.getTrigger(id)

    // Todo foreign key to mock
    @PostMapping("/triggers")
    fun addTrigger(
        @RequestAttribute username: String,
        @RequestBody trigger: TriggerDto,
    ): StringIdResponse {
        val createdTriggerId = triggerService.addTrigger(trigger, username)

        return StringIdResponse(createdTriggerId)
    }

    @PutMapping("/triggers/{id}")
    fun patchTrigger(
        @PathVariable id: Long,
        @RequestAttribute username: String,
        @RequestBody trigger: TriggerDto,
    ) = triggerService.patchTrigger(id, trigger, username)

    @PatchMapping("/triggers/{id}")
    fun activateTriggers(
        @PathVariable id: Long,
        @RequestAttribute username: String,
        @RequestBody request: ActivateRequestDto,
    ) = triggerService.activateTrigger(id, request, username)

    @DeleteMapping("/triggers/{id}")
    fun deleteTrigger(@PathVariable id: Long, @RequestAttribute username: String): StringIdResponse {
        triggerService.deleteTrigger(id, username)

        return StringIdResponse(id)
    }
}
