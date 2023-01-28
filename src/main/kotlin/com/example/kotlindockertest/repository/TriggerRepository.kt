package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.trigger.TriggerDto
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface TriggerRepository : CrudRepository<TriggerDto, Long> {

    @Query(
        """
        SELECT new com.example.kotlindockertest.model.trigger.TriggerDto(t.id, t.path, t.mockId) 
        FROM TriggerDto t
        WHERE t.mockId = ?1
        """
    )
    fun getTriggers(mockId: Long): List<TriggerDto>
}
