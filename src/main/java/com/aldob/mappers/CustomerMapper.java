package com.aldob.mappers;

import org.springframework.stereotype.Component;

import com.aldob.dtos.responses.CustomerTicketResponse;
import com.aldob.entities.Ticket;

@Component
public class CustomerMapper {

    public CustomerTicketResponse toCustomerTicketResponse(Ticket ticket) {
        CustomerTicketResponse customerTicketResponse = new CustomerTicketResponse();
        customerTicketResponse.setId(ticket.getId());
        customerTicketResponse.setTitle(ticket.getTitle());
        customerTicketResponse.setPriority(ticket.getPriority());
        customerTicketResponse.setStatus(ticket.getStatus());
        customerTicketResponse.setCreatedAt(ticket.getCreatedAt());
        return customerTicketResponse;
    }

}
