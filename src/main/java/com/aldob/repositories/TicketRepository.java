package com.aldob.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aldob.dtos.responses.TicketStatisticsResponse;
import com.aldob.entities.Ticket;
import com.aldob.enums.Priority;
import com.aldob.enums.Status;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByIdempotencyKey(String idempotencyKey);

    @Query("SELECT id, title, priority, status, createdAt FROM Ticket t WHERE t.customer.id = :customerId")
    List<Ticket> findByCustomerId(Long customerId);

    @Query("""
        SELECT t
        FROM Ticket t
        WHERE (:status IS NULL OR t.status = :status)
          AND (:priority IS NULL OR t.priority = :priority)
          AND (:customerId IS NULL OR t.customer.id = :customerId)
        ORDER BY t.createdAt DESC
    """)
    List<Ticket> findByStatusPriorityAndCustomerId(
        @Param("status") Status status,
        @Param("priority") Priority priority,
        @Param("customerId") Long customerId
    );

    @Query("""
        SELECT new com.aldob.dtos.responses.TicketStatisticsResponse(
            COUNT(t),
            COALESCE(SUM(CASE WHEN t.status = com.aldob.enums.Status.OPEN THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.status = com.aldob.enums.Status.IN_PROGRESS THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.status = com.aldob.enums.Status.RESOLVED THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.status = com.aldob.enums.Status.CLOSED THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.priority = com.aldob.enums.Priority.CRITICAL THEN 1 ELSE 0 END), 0)
        )
        FROM Ticket t
    """)
    TicketStatisticsResponse getTicketsStats();

}
