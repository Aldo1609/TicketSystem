package com.aldob.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aldob.entities.TicketComment;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {

    @Query("SELECT tc FROM TicketComment tc WHERE tc.ticket.id = :ticketId ORDER BY tc.createdAt ASC")
    List<TicketComment> findByTicketId(Long ticketId);

}
