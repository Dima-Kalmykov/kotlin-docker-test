package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.trigger.TriggerDto
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import javax.swing.text.html.Option

interface TriggerRepository : CrudRepository<TriggerDto, Long> {

    @Query(
        """
        SELECT new com.example.kotlindockertest.model.trigger.TriggerDto(t.id, t.path, t.mockId, t.createdBy, t.operation, t.value, t.enable) 
        FROM TriggerDto t
        WHERE t.mockId = ?1
        """
    )
    fun getTriggers(mockId: Long): List<TriggerDto>


    @Modifying
    @Query(
        """
            DELETE FROM TriggerDto t 
            WHERE t.mockId = ?1
        """
    )
    fun deleteTriggersByMockId(mockId: Long)
}
