package com.aldob.dtos.requests;

import com.aldob.enums.Status;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateStatusRequest {

    @NotNull(message = "status is required")
    private Status status;

}
