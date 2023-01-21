package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.service.MockServiceDto
import com.example.kotlindockertest.model.service.MockServiceShortInfoDto
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface MockServiceRepository : CrudRepository<MockServiceDto, Long> {

    @Query(
        """
        SELECT new com.example.kotlindockertest.model.service.MockServiceShortInfoDto(s.id, s.name, s.host) 
        FROM MockServiceDto s
        """
    )
    fun getServices(): List<MockServiceShortInfoDto>
}
