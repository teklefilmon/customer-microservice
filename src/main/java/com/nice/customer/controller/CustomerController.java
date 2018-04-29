package com.nice.customer.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nice.customer.domain.Customer;
import com.nice.customer.service.CustomerService;

@RestController
public class CustomerController {

	private CustomerService customerService;

	public CustomerController(CustomerService service) {
		Objects.requireNonNull(service, "CustomerService is required for the class to function");
		this.customerService = service;
	}

	@GetMapping(value = "/customers/{idOrUsername}")
	ResponseEntity<Customer> getUser(@PathVariable String idOrUsername) {
		Customer customer = null;
		try {
			Long id = Long.parseLong(idOrUsername);
			customer = customerService.findOne(id);
		} catch (NumberFormatException exception) {
			customer = customerService.findOne(idOrUsername);
		}
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	
	@GetMapping(value = "/customers")
	ResponseEntity<List<Customer>> getAllUsers() {
		return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
	}

	@PostMapping(value="/customers")
	ResponseEntity<Void> createUser(@Valid @RequestBody Customer customer){
		Customer newUser = customerService.create(customer);
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserUri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        responseHeaders.setLocation(newUserUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@PutMapping(value="/customers/{id}")
	ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody @Valid Customer customer) {
		customerService.update(id, customer);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/customers/{id}")
	ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		customerService.delete(id);
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
