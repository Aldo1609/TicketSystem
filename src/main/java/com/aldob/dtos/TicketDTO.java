package com.aldob.dtos;

import com.aldob.enums.Priority;
import com.aldob.enums.Status;

import lombok.Data;

@Data
public class TicketDTO {

    private Long id;
    private Long customerId;
    private String title;
    private String description;
    private Priority priority;
    private Status status;

}
