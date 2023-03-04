package com.example.kotlindockertest.controller

import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.TriggerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/grpaphql")
class TriggerController(private val triggerService: TriggerService) {

    @GetMapping("/mocks/{mockId}/triggers")
    fun getTriggers(@PathVariable mockId: Long) = triggerService.getTriggers(mockId)

    @GetMapping("/triggers/{id}")
    fun getTrigger(@PathVariable id: Long) = triggerService.getTrigger(id)

    // Todo foreign key to mock
    @PostMapping("/triggers")
    fun addTrigger(@RequestBody trigger: TriggerDto): StringIdResponse {
        val createdTriggerId = triggerService.addTrigger(trigger)

        return StringIdResponse(createdTriggerId)
    }

    @PutMapping("/triggers/{id}")
    fun patchTrigger(
        @PathVariable id: Long,
        @RequestBody trigger: TriggerDto,
    ) = triggerService.patchTrigger(id, trigger)

    @DeleteMapping("/triggers/{id}")
    fun deleteTrigger(@PathVariable id: Long): StringIdResponse {
        triggerService.deleteTrigger(id)

        return StringIdResponse(id)
    }
}
