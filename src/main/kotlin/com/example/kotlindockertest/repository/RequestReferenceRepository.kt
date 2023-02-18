package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.RequestReference
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository


interface RequestReferenceRepository : CrudRepository<RequestReference, Long> {

    fun findAllByMockId(mockId: Long): List<RequestReference>
}
