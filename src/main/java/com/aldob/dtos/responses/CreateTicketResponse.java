package com.aldob.dtos.responses;

import java.time.OffsetDateTime;

import com.aldob.enums.Priority;
import com.aldob.enums.Status;

import lombok.Data;

@Data
public class CreateTicketResponse {

    private Long id;
    private Long customerId;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
