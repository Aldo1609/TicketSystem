package com.aldob.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aldob.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
