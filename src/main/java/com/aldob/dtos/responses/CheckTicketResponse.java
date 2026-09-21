package com.aldob.dtos.responses;


import java.time.OffsetDateTime;

import com.aldob.dtos.CustomerDTO;

import lombok.Data;

@Data
public class CheckTicketResponse {

    private Long id;
    private CustomerDTO customer;
    private String title;
    private String description;
    private String priority;
    private String status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
