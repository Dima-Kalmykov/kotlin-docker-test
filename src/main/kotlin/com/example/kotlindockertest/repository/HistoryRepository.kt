package com.example.kotlindockertest.repository

import com.example.kotlindockertest.model.history.HistoryEventDto
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.ZonedDateTime

interface HistoryRepository : PagingAndSortingRepository<HistoryEventDto, Long> {

    @Modifying
    @Query(
        """
            DELETE FROM HistoryEventDto h
            WHERE h.expirationDate < CURRENT_TIMESTAMP()
        """
    )
    fun removeHistoryEventsByTtl()

    @Query(
        """
            SELECT new HistoryEventDto(h.id, h.serviceId, h.createdAt, h.expirationDate, h.redirected, h.isError, h.request, h.response)
            FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
        """
    )
    fun findAllByServiceId(
        serviceId: Long,
        from: ZonedDateTime,
        to: ZonedDateTime,
        pageable: Pageable
    ): List<HistoryEventDto>

    @Query(
        """
            SELECT new HistoryEventDto(h.id, h.serviceId, h.createdAt, h.expirationDate, h.redirected, h.isError, h.request, h.response)
            FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
            AND h.isError = ?4
            AND h.redirected = ?5
        """
    )
    fun findWithErrorAndRedirected(
        serviceId: Long,
        from: ZonedDateTime,
        to: ZonedDateTime,
        isError: Boolean,
        redirected: Boolean,
        pageable: Pageable,
    ): List<HistoryEventDto>

    @Query(
        """
            SELECT new HistoryEventDto(h.id, h.serviceId, h.createdAt, h.expirationDate, h.redirected, h.isError, h.request, h.response)
            FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
            AND h.isError = ?4
        """
    )
    fun findWithError(
        serviceId: Long,
        from: ZonedDateTime,
        to: ZonedDateTime,
        isError: Boolean,
        pageable: Pageable,
    ): List<HistoryEventDto>

    @Query(
        """
            SELECT new HistoryEventDto(h.id, h.serviceId, h.createdAt, h.expirationDate, h.redirected, h.isError, h.request, h.response)
            FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
            AND h.redirected = ?4
        """
    )
    fun findWithRedirected(
        serviceId: Long,
        from: ZonedDateTime,
        to: ZonedDateTime,
        redirected: Boolean,
        pageable: Pageable,
    ): List<HistoryEventDto>


    @Query(
        """
            SELECT COUNT(*) FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
        """
    )
    fun getCountByServiceId(serviceId: Long, from: ZonedDateTime, to: ZonedDateTime): Int

    @Query(
        """
            SELECT COUNT(*) FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
            AND h.isError = ?4
        """
    )
    fun getCountWithError(serviceId: Long, from: ZonedDateTime, to: ZonedDateTime, isError: Boolean): Int


    @Query(
        """
            SELECT COUNT(*) FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
            AND h.redirected = ?4
        """
    )
    fun getCountWithRedirected(serviceId: Long, from: ZonedDateTime, to: ZonedDateTime, redirected: Boolean): Int

    @Query(
        """
            SELECT COUNT(*) FROM HistoryEventDto h
            WHERE h.serviceId = ?1 
            AND h.expirationDate >= CURRENT_TIMESTAMP()
            AND h.createdAt >= ?2
            AND h.createdAt <= ?3
            AND h.isError = ?4
            AND h.redirected = ?5
        """
    )
    fun getCountWithErrorAndRedirected(
        serviceId: Long, from: ZonedDateTime, to: ZonedDateTime, isError: Boolean, redirected: Boolean,
        ): Int

    fun findAllByServiceId(serviceId: Long): List<HistoryEventDto>
}
