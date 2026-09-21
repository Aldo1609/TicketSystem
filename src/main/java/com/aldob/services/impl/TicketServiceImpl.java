package com.aldob.services.impl;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.aldob.dtos.requests.CreateTicketRequest;
import com.aldob.dtos.requests.UpdateStatusRequest;
import com.aldob.dtos.responses.CheckTicketResponse;
import com.aldob.dtos.responses.CreateTicketResponse;
import com.aldob.dtos.responses.SearchTicketsResponse;
import com.aldob.dtos.responses.TicketStatisticsResponse;
import com.aldob.dtos.responses.UpdateStatusResponse;
import com.aldob.entities.Customer;
import com.aldob.entities.Ticket;
import com.aldob.enums.Priority;
import com.aldob.enums.Status;
import com.aldob.exceptions.customs.CustomerNotActiveException;
import com.aldob.exceptions.customs.CustomerNotFoundException;
import com.aldob.exceptions.customs.InvalidTicketStatusTransitionException;
import com.aldob.exceptions.customs.TicketNotFoundException;
import com.aldob.mappers.TicketMapper;

import com.aldob.repositories.CustomerRepository;
import com.aldob.repositories.TicketRepository;
import com.aldob.services.TicketService;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final TicketMapper ticketMapper;

    @Override
    public CreateTicketResponse createTicket(
            CreateTicketRequest request,
            String idempotencyKey
    ) {
        var existingTicket = ticketRepository.findByIdempotencyKey(idempotencyKey);

        if (existingTicket.isPresent()) {
            return ticketMapper.ticketEntityToCreateTicketResponse(existingTicket.get());
        }

        try {
            Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

            if (!customer.getActive()) {
                throw new CustomerNotActiveException("Customer is not active");
            }

            Ticket ticket = ticketMapper.requestCreateTicketToEntity(request, customer, idempotencyKey);
            Ticket savedTicket = ticketRepository.saveAndFlush(ticket);

            return ticketMapper.ticketEntityToCreateTicketResponse(savedTicket);
        } catch (DataIntegrityViolationException exception) {
            return ticketRepository.findByIdempotencyKey(idempotencyKey)
                .map(ticketMapper::ticketEntityToCreateTicketResponse)
                .orElseThrow(() -> exception);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CheckTicketResponse checkTicket(Long id) {

        Ticket ticket = ticketRepository
            .findById(id)
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        return ticketMapper.ticketEntityToCheckTicketResponse(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchTicketsResponse> searchTickets(Status status, Priority priority, Long customerId) {

        return ticketRepository.findByStatusPriorityAndCustomerId(status, priority, customerId)
            .stream()
            .map(ticketMapper::ticketEntityToSearchTicketResponse)
            .toList();
    }

    @Override
    @Transactional
    public UpdateStatusResponse updateTicketStatus(
            Long id,
            UpdateStatusRequest request
    ) {
        Ticket ticket = ticketRepository
            .findById(id)
            .orElseThrow(() ->
                new TicketNotFoundException("Ticket not found")
            );

        Status currentStatus = ticket.getStatus();
        Status requestedStatus = request.getStatus();

        if (!isValidStatusTransition(currentStatus, requestedStatus)) {
            throw new InvalidTicketStatusTransitionException(
                "Cannot change ticket status from "
                    + currentStatus + " to " + requestedStatus
            );
        }

        if (currentStatus != requestedStatus) {
            ticket.setStatus(requestedStatus);
            ticketRepository.saveAndFlush(ticket);
        }

        return ticketMapper
            .ticketEntityToUpdateStatusResponse(ticket);
    }

    private boolean isValidStatusTransition(
            Status currentStatus,
            Status requestedStatus
    ) {
        if (currentStatus == requestedStatus) {
            return true;
        }

        return switch (currentStatus) {
            case OPEN -> requestedStatus == Status.IN_PROGRESS;
            case IN_PROGRESS -> requestedStatus == Status.RESOLVED;
            case RESOLVED -> requestedStatus == Status.CLOSED;
            case CLOSED -> false;
        };
    }

    @Override
    @Transactional(readOnly = true)
    public TicketStatisticsResponse getTicketStatistics() {
        return ticketRepository.getTicketsStats();
    }

}
