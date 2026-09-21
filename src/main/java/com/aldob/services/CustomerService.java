package com.aldob.services;

import java.util.List;

import com.aldob.dtos.responses.CustomerTicketResponse;

public interface CustomerService {

    List<CustomerTicketResponse> getCustomerTickets(Long customerId);

}
