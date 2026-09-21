package com.aldob.dtos.responses;

import java.time.OffsetDateTime;

import com.aldob.enums.Priority;
import com.aldob.enums.Status;

import lombok.Data;

@Data
public class SearchTicketsResponse {

    private Long id;
    private Long customerId;
    private String title;
    private Priority priority;
    private Status status;
    private OffsetDateTime createdAt;
}
