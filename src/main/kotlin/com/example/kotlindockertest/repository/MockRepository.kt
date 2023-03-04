package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.mock.MockDto
import com.example.kotlindockertest.model.mock.MockShortInfoDto
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface MockRepository : CrudRepository<MockDto, Long> {

    @Query(
        """
        SELECT new com.example.kotlindockertest.model.mock.MockShortInfoDto(m.id, m.name) 
        FROM MockDto m
        WHERE m.serviceId = ?1
        """
    )
    fun getMocks(serviceId: Long): List<MockShortInfoDto>

    fun findByServiceIdAndRequestHash(serviceId: Long, requestHash: Int): Optional<MockDto>

    @Modifying
    @Query(
        """
            DELETE FROM MockDto m
            WHERE m.serviceId = ?1
        """
    )
    fun deleteMocksByServiceId(serviceId: Long)

    @Modifying
    @Query(
        """
            DELETE FROM MockDto m
            WHERE m.ttlDateTime < CURRENT_TIMESTAMP()
        """
    )
    fun deleteMocksByTtl()

    fun findByServiceIdAndName(serviceId: Long, name: String): Optional<MockDto>
}
