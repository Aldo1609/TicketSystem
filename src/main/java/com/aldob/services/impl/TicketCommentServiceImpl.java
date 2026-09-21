package com.aldob.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aldob.dtos.requests.AddCommentRequest;
import com.aldob.dtos.responses.AddCommentResponse;
import com.aldob.dtos.responses.GetCommentsResponse;
import com.aldob.entities.Ticket;
import com.aldob.entities.TicketComment;
import com.aldob.repositories.TicketCommentRepository;
import com.aldob.repositories.TicketRepository;
import com.aldob.services.TicketCommentService;

import com.aldob.exceptions.customs.TicketNotFoundException;
import com.aldob.mappers.TicketCommentMapper;

@Service
@AllArgsConstructor
@Slf4j
public class TicketCommentServiceImpl implements TicketCommentService {

    private final TicketRepository ticketRepository;
    private final TicketCommentMapper ticketCommentMapper;
    private final TicketCommentRepository ticketCommentRepository;

    @Override
    public AddCommentResponse addComment(Long ticketId, AddCommentRequest request) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        TicketComment comment = ticketCommentMapper.toEntity(ticket, request);
        TicketComment savedComment = ticketCommentRepository.saveAndFlush(comment);

        return ticketCommentMapper.toResponse(savedComment);
    }

    @Override
    public List<GetCommentsResponse> getComments(Long ticketId) {

        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException("Ticket not found");
        }

        return ticketCommentRepository.findByTicketId(ticketId)
                .stream()
                .map(ticketCommentMapper::toGetCommentsResponse)
                .toList();
    }

}
