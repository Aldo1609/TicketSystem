package com.aldob.dtos;

import lombok.Data;

@Data
public class TicketCommentDTO {

    private Long id;
    private Long ticketId;
    private String authorName;
    private String message;

}
