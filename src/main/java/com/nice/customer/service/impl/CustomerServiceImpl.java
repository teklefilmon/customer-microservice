package com.nice.customer.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.nice.customer.domain.Customer;
import com.nice.customer.exception.CustomerNotFoundException;
import com.nice.customer.exception.UsernameTakenException;
import com.nice.customer.repository.CustomerRepository;
import com.nice.customer.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);
	private CustomerRepository repository;

	public CustomerServiceImpl(CustomerRepository reposioty) {
		this.repository = reposioty;
	}

	@Override
	public Customer findOne(Long id) {
		Objects.requireNonNull(id, "Id is required to find customer");
		Optional<Customer> customer = this.repository.findById(id);
		if (customer.isPresent()) {
			logger.info(String.format("Customer with id %s retrieved", id));
			return customer.get();
		}
		logger.error(String.format("Customer with id %s not found", id));
		throw new CustomerNotFoundException(String.format("Customer with id %s not found", id));
	}

	@Override
	public Customer findOne(String username) {
		Assert.hasText(username, "Username is requied to find customer");
		Optional<Customer> customer = this.repository.findByUserName(username);
		if (customer.isPresent()) {
			logger.info(String.format("Customer with username %s retrieved", username));
			return customer.get();
		}
		logger.error(String.format("Customer with username %s not found", username));
		throw new CustomerNotFoundException(String.format("Customer with username %s not found", username));
	}

	@Override
	public List<Customer> findAll() {
		return repository.findAll();
	}
	
	@Override
	public Customer create(@Valid Customer customer) {
		String username = customer.getUserName();
		try{
			logger.info(String.format("Checking if username %s is available", username));
			findOne(username);
			throw new UsernameTakenException(String.format("Username %s already taken", username));
		} catch(CustomerNotFoundException exception) {
			Customer persistedUser = repository.save(customer);
			logger.info("Customer saved");
			return persistedUser;
		}
	}

	@Override
	public void update(Long id, Customer customer) {
		Objects.requireNonNull(id, "Id is required to update customer");
		Customer retrievedUser = findOne(id);
		retrievedUser.setFirstName(customer.getFirstName());
		retrievedUser.setLastName(customer.getLastName());
		retrievedUser.setEmail(customer.getEmail());
		retrievedUser.setUserName(customer.getUserName());
		this.repository.save(retrievedUser);
		logger.info(String.format("Customer with id %s updated", id));
	}

	@Override
	public void delete(Long id) {
		Objects.requireNonNull(id, "Id must not be null");
		findOne(id);
		repository.deleteById(id);
		logger.info(String.format("Customer with id %s deleted", id));
	}


}
