package com.aldob.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aldob.dtos.responses.GetCommentsResponse;
import com.aldob.entities.TicketComment;
import com.aldob.exceptions.customs.TicketNotFoundException;
import com.aldob.mappers.TicketCommentMapper;
import com.aldob.repositories.TicketCommentRepository;
import com.aldob.repositories.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class TicketCommentServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketCommentMapper ticketCommentMapper;

    @Mock
    private TicketCommentRepository ticketCommentRepository;

    @InjectMocks
    private TicketCommentServiceImpl ticketCommentService;


    @Test
    public void shouldReturnTicketComments() {

        // ARRANGE
        // Given a ticketId, a ticketComment, and a comments response
        Long ticketId = 1L;

        TicketComment ticketComment = new TicketComment();

        GetCommentsResponse comments = new GetCommentsResponse();

        when(ticketRepository.existsById(ticketId))
            .thenReturn(true);

        when(ticketCommentRepository.findByTicketId(ticketId))
            .thenReturn(List.of(ticketComment));

        when(ticketCommentMapper.toGetCommentsResponse(ticketComment))
            .thenReturn(comments);

        // ACT
        // When the service is called to get comments by ticketId

        List<GetCommentsResponse> result = ticketCommentService.getComments(ticketId);

        // ASSERT
        // Then the result should contain the comments response
        assertEquals(1, result.size());

        verify(ticketRepository)
                .existsById(ticketId);

        verify(ticketCommentMapper)
                .toGetCommentsResponse(ticketComment);

        verify(ticketCommentRepository)
                .findByTicketId(ticketId);
    }

    @Test
    public void shouldReturnEmptyListWhenTicketNotFound() {

        // ARRANGE

        Long ticketId = 1L;

        when(ticketRepository.existsById(ticketId))
            .thenReturn(false);

        // ACT + ASSERT

        assertThrows(TicketNotFoundException.class,
            () -> ticketCommentService.getComments(ticketId));

        // VERIFY

        verify(ticketRepository)
            .existsById(ticketId);

        verify(ticketCommentRepository, never())
            .findByTicketId(ticketId);
    }
}
