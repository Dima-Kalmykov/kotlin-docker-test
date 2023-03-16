package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.service.MockServiceShortInfoDto
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface MockServiceRepository : CrudRepository<MockServiceDto, Long> {

    @Query(
        """
        SELECT new com.example.kotlindockertest.model.service.MockServiceShortInfoDto(s.id, s.name, s.location) 
        FROM MockServiceDto s
        WHERE s.name LIKE %?1% OR s.location LIKE %?1%
        """
    )
    fun getServices(search: String): List<MockServiceShortInfoDto>

    fun findByName(name: String): Optional<MockServiceDto>

    @Modifying
    @Query(
        """
            DELETE FROM MockServiceDto m
            WHERE m.expirationDate < CURRENT_TIMESTAMP()
        """
    )
    fun deleteServicesByTtl()
}
