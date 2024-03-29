package com.example.kotlindockertest.controller

import com.example.kotlindockertest.exception.TriggerException
import com.example.kotlindockertest.model.ActivateRequestDto
import com.example.kotlindockertest.model.StringErrorResponse
import com.example.kotlindockertest.model.StringIdResponse
import com.example.kotlindockertest.model.trigger.TriggerDto
import com.example.kotlindockertest.service.TriggerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.print.DocFlavor.STRING

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
    ): ResponseEntity<StringIdResponse> {
        return try {
            val createdTriggerId = triggerService.addTrigger(trigger, username)

            ResponseEntity.ok(StringIdResponse(createdTriggerId))
        } catch (ex: Exception) {
            throw TriggerException(ex.message)
        }
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
