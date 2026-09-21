package com.aldob.services.impl;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aldob.dtos.responses.CustomerTicketResponse;
import com.aldob.exceptions.customs.CustomerNotFoundException;
import com.aldob.mappers.CustomerMapper;
import com.aldob.repositories.CustomerRepository;
import com.aldob.repositories.TicketRepository;
import com.aldob.services.CustomerService;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerTicketResponse> getCustomerTickets(Long customerId) {

        if(!customerRepository.existsById(customerId)){
            throw new CustomerNotFoundException("Customer not found");
        }

        return ticketRepository.findByCustomerId(customerId)
            .stream()
            .map(customerMapper::toCustomerTicketResponse)
            .toList();
    }
}
