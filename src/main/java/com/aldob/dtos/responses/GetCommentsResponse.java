package com.aldob.dtos.responses;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class GetCommentsResponse {

    private Long id;
    private String authorName;
    private String message;
    private OffsetDateTime createdAt;

}
