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
        SELECT new com.example.kotlindockertest.model.service.MockServiceShortInfoDto(s.id, s.name, s.location, s.createdBy, s.expirationDate) 
        FROM MockServiceDto s
        WHERE UPPER(s.name) LIKE %?1% OR UPPER(s.location) LIKE %?1%
        """
    )
    fun getServices(search: String): List<MockServiceShortInfoDto>

    fun findByName(name: String): Optional<MockServiceDto>

    @Modifying
    @Query(
        """
            DELETE FROM MockServiceDto s
            WHERE s.expirationDate < CURRENT_TIMESTAMP()
        """
    )
    fun deleteServicesByTtl()

    @Query(
        """
            SELECT new com.example.kotlindockertest.model.service.MockServiceShortInfoDto(s.id, s.name, s.location, s.createdBy, s.expirationDate) 
            FROM MockServiceDto s
            WHERE s.expirationDate < CURRENT_TIMESTAMP()
        """
    )
    fun getServicesToBeDeleted(): List<MockServiceShortInfoDto>
}
