package com.nice.customer.service;

import java.util.List;

import com.nice.customer.domain.Customer;

public interface CustomerService {
	Customer findOne(Long id);
	Customer findOne(String username);
	List<Customer> findAll();
	Customer create(Customer customer);
	void update(Long id, Customer customer);
	void delete(Long id);
}
