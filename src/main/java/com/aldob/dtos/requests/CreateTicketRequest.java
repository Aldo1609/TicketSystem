package com.aldob.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateTicketRequest {

    @NotNull(message = "customerId is required")
    @Min(value = 1, message = "customerId must be greater than 0")
    private Long customerId;

    @NotBlank(message = "title is required")
    @Min(value = 5, message = "title must be at least 5 characters")
    @Max (value = 150, message = "title must be at most 150 characters")
    private String title;

    @NotBlank(message = "description is required")
    @Min(value = 10, message = "description must be at least 10 characters")
    private String description;

    @NotNull(message = "priority is required")
    @Pattern(regexp = "LOW|MEDIUM|HIGH|CRITICAL", message = "priority should be LOW, MEDIUM, HIGH or CRITICAL")
    private String priority;

}
