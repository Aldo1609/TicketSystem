package com.aldob.mappers;

import org.springframework.stereotype.Component;

import com.aldob.dtos.CustomerDTO;
import com.aldob.dtos.requests.CreateTicketRequest;
import com.aldob.dtos.responses.CheckTicketResponse;
import com.aldob.dtos.responses.CreateTicketResponse;
import com.aldob.dtos.responses.SearchTicketsResponse;
import com.aldob.dtos.responses.UpdateStatusResponse;
import com.aldob.entities.Customer;
import com.aldob.entities.Ticket;
import com.aldob.enums.Priority;
import com.aldob.enums.Status;

@Component
public class TicketMapper {

    public Ticket requestCreateTicketToEntity(
        CreateTicketRequest request,
        Customer customer,
        String idempotencyKey
    ) {
        Ticket ticket = new Ticket();
        ticket.setCustomer(customer);
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(Priority.valueOf(request.getPriority()));
        ticket.setStatus(Status.OPEN);
        ticket.setIdempotencyKey(idempotencyKey);
        return ticket;
    }

    public CreateTicketResponse ticketEntityToCreateTicketResponse(
        Ticket ticket
    ) {
        CreateTicketResponse response = new CreateTicketResponse();
        response.setId(ticket.getId());
        response.setCustomerId(ticket.getCustomer().getId());
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setPriority(ticket.getPriority());
        response.setStatus(ticket.getStatus());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setUpdatedAt(ticket.getUpdatedAt());
        return response;
    }

    public CheckTicketResponse ticketEntityToCheckTicketResponse(
        Ticket ticket
    ) {
        CheckTicketResponse response = new CheckTicketResponse();
        response.setId(ticket.getId());
        Customer customer = ticket.getCustomer();
        CustomerDTO customerResponse = new CustomerDTO();
        customerResponse.setId(customer.getId());
        customerResponse.setName(customer.getName());
        customerResponse.setEmail(customer.getEmail());

        response.setCustomer(customerResponse);
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setPriority(ticket.getPriority().name());
        response.setStatus(ticket.getStatus().name());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setUpdatedAt(ticket.getUpdatedAt());
        return response;
    }

    public SearchTicketsResponse ticketEntityToSearchTicketResponse(
        Ticket ticket
    ) {
        SearchTicketsResponse response = new SearchTicketsResponse();
        response.setId(ticket.getId());
        response.setCustomerId(ticket.getCustomer().getId());
        response.setTitle(ticket.getTitle());
        response.setPriority(ticket.getPriority());
        response.setStatus(ticket.getStatus());
        response.setCreatedAt(ticket.getCreatedAt());
        return response;
    }

    public UpdateStatusResponse ticketEntityToUpdateStatusResponse(
        Ticket ticket
    ) {
        UpdateStatusResponse response = new UpdateStatusResponse();
        response.setId(ticket.getId());
        response.setStatus(ticket.getStatus());
        response.setUpdatedAt(ticket.getUpdatedAt());
        return response;
    }

}
