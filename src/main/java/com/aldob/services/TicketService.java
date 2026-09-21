package com.aldob.services;

import java.util.List;

import com.aldob.dtos.requests.CreateTicketRequest;
import com.aldob.dtos.requests.UpdateStatusRequest;
import com.aldob.dtos.responses.CheckTicketResponse;
import com.aldob.dtos.responses.CreateTicketResponse;
import com.aldob.dtos.responses.SearchTicketsResponse;
import com.aldob.dtos.responses.TicketStatisticsResponse;
import com.aldob.dtos.responses.UpdateStatusResponse;
import com.aldob.enums.Priority;
import com.aldob.enums.Status;

public interface TicketService {

    CreateTicketResponse createTicket(
            CreateTicketRequest request,
            String idempotencyKey
        );

    CheckTicketResponse checkTicket(Long id);

    List<SearchTicketsResponse> searchTickets(
        Status status,
        Priority priority,
        Long customerId
    );

    UpdateStatusResponse updateTicketStatus(Long id, UpdateStatusRequest request);

    TicketStatisticsResponse getTicketStatistics();

}
