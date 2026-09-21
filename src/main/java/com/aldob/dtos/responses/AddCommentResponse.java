package com.aldob.dtos.responses;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class AddCommentResponse {

    private Long id;
    private Long ticketId;
    private String authorName;
    private String message;
    private OffsetDateTime createdAt;

}
