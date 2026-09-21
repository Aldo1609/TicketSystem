package com.aldob.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aldob.dtos.payload.ApiResponse;
import com.aldob.dtos.requests.CreateTicketRequest;
import com.aldob.dtos.requests.UpdateStatusRequest;
import com.aldob.dtos.responses.CheckTicketResponse;
import com.aldob.dtos.responses.CreateTicketResponse;
import com.aldob.dtos.responses.SearchTicketsResponse;
import com.aldob.dtos.responses.TicketStatisticsResponse;
import com.aldob.dtos.responses.UpdateStatusResponse;
import com.aldob.enums.Priority;
import com.aldob.enums.Status;
import com.aldob.services.TicketService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(path = "tickets")
    @Tag(name = "Ticket Controller", description = "Create a new ticket")
    public ResponseEntity<ApiResponse<CreateTicketResponse>> createTicket(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateTicketRequest request
    ) {

        CreateTicketResponse ticket = ticketService.createTicket(request, idempotencyKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(
                true,
                "Ticket created succesfully",
                ticket
            )
        );
    }

    @GetMapping(path = "/tickets/{id}")
    @Tag(name = "Ticket Controller", description = "Check a ticket by id")
    public ResponseEntity<ApiResponse<CheckTicketResponse>> CheckTicketById(@PathVariable Long id) {

        CheckTicketResponse ticket = ticketService.checkTicket(id);

        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                true,
                "Ticket retrieved succesfully",
                ticket
            )
        );
    }

    @GetMapping(path = "/tickets")
    @Tag(name = "Ticket Controller", description = "Search tickets")
    public ResponseEntity<ApiResponse<List<SearchTicketsResponse>>> searchTickets(
            @RequestParam (required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long customerId
    ) {

        List<SearchTicketsResponse> tickets = ticketService.searchTickets(status, priority, customerId);

        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                true,
                "Ticket retrieved succesfully",
                tickets
            )
        );
    }

    @PatchMapping(path = "/tickets/{id}/status")
    @Tag(name = "Ticket Controller", description = "Update ticket status")
    public ResponseEntity<ApiResponse<UpdateStatusResponse>> updateTicketStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {

        UpdateStatusResponse statusTicket = ticketService.updateTicketStatus(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                true,
                "Status updated succesfully",
                statusTicket
            )
        );

    }

    @GetMapping(path = "/tickets/statistics")
    @Tag(name = "Ticket Controller", description = "Get ticket statistics")
    public ResponseEntity<ApiResponse<TicketStatisticsResponse>> getTicketStatistics() {

        TicketStatisticsResponse statistics = ticketService.getTicketStatistics();

        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                true,
                "Ticket statistics retrieved succesfully",
                statistics
            )
        );
    }

}
