package com.aldob.controllers;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aldob.dtos.payload.ApiResponse;
import com.aldob.dtos.responses.CustomerTicketResponse;
import com.aldob.services.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(path = "/customers/{customerId}/tickets")
    @Tag(name = "Customer Controller", description = "Get customer tickets by customer ID")
    public ResponseEntity<ApiResponse<List<CustomerTicketResponse>>> getCustomerTickets(@PathVariable Long customerId) {

        List<CustomerTicketResponse> tickets = customerService.getCustomerTickets(customerId);

        return ResponseEntity.ok(
            new ApiResponse<>(
                true,
                "Tickets retrieved successfully",
                tickets
            )
        );

    }

}
