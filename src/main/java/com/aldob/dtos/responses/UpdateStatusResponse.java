package com.aldob.dtos.responses;

import java.time.OffsetDateTime;

import com.aldob.enums.Status;

import lombok.Data;

@Data
public class UpdateStatusResponse {

    private Long id;
    private Status status;
    private OffsetDateTime updatedAt;

}
