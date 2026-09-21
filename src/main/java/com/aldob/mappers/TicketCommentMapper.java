package com.aldob.mappers;

import org.springframework.stereotype.Component;

import com.aldob.dtos.requests.AddCommentRequest;
import com.aldob.dtos.responses.AddCommentResponse;
import com.aldob.dtos.responses.GetCommentsResponse;
import com.aldob.entities.Ticket;
import com.aldob.entities.TicketComment;

@Component
public class TicketCommentMapper {

    public TicketComment toEntity(Ticket ticket, AddCommentRequest request) {
        TicketComment ticketComment = new TicketComment();
        ticketComment.setTicket(ticket);
        ticketComment.setAuthorName(request.getAuthorName());
        ticketComment.setMessage(request.getMessage());
        return ticketComment;
    }

    public AddCommentResponse toResponse(TicketComment ticketComment) {
        AddCommentResponse response = new AddCommentResponse();
        response.setId(ticketComment.getId());
        response.setTicketId(ticketComment.getTicket().getId());
        response.setAuthorName(ticketComment.getAuthorName());
        response.setMessage(ticketComment.getMessage());
        response.setCreatedAt(ticketComment.getCreatedAt());
        return response;
    }

    public GetCommentsResponse toGetCommentsResponse(TicketComment ticketComment) {
        GetCommentsResponse response = new GetCommentsResponse();
        response.setId(ticketComment.getId());
        response.setAuthorName(ticketComment.getAuthorName());
        response.setMessage(ticketComment.getMessage());
        response.setCreatedAt(ticketComment.getCreatedAt());
        return response;
    }

}
