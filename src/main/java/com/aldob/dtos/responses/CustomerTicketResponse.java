package com.aldob.dtos.responses;

import java.time.OffsetDateTime;
import com.aldob.enums.Priority;
import com.aldob.enums.Status;

import lombok.Data;

@Data
public class CustomerTicketResponse {

    private Long id;
    private String title;
    private Priority priority;
    private Status status;
    private OffsetDateTime createdAt;

}
