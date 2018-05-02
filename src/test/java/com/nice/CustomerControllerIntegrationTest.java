package com.nice;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.nice.customer.domain.Customer;


public class CustomerControllerIntegrationTest extends BaseIntegrationTest{
	@LocalServerPort
	private int port;
	
	@Test
	public void getCustomer_whenCustomerIdNotExist_shouldReturnNotFound() 
	{
		given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", 100)
		.when()
			.get("/customers/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value())
			.body("title", equalTo("Customer Not Found"))
			.body("detail", equalTo("Customer with id " + 100 +" not found"));
	}
	
	@Test
	public void getCustomer_whenCustomerIdExist_shouldReturnOK() 
	{
		
		Customer customer = given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", 1)
		.when()
			.get("/customers/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.OK.value())
				.and()
			.extract().as(Customer.class);
		
		assertThat(customer.getFirstName(), equalTo("James"));
		assertThat(customer.getLastName(), equalTo("Alexander"));
		assertThat(customer.getEmail(), equalTo("james.alexander@nice.com"));
		assertThat(customer.getUserName(), equalTo("james.alexander"));
	}
	
	@Test
	public void getCustomer_whenCustomerNameNotExist_shouldReturnNotFound() 
	{
		given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", "david.john")
		.when()
			.get("/customers/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void getCustomer_whenCustomerNameExist_shouldReturnOK() 
	{
		Customer customer = given()
			.spec(spec)
			.port(port)
			.pathParam("idOrUsername", "james.alexander")
		.when()
			.get("/customers/{idOrUsername}")
		.then()
			.statusCode(HttpStatus.OK.value())
				.and()
			.extract().as(Customer.class);
		
		assertThat(customer.getFirstName(), equalTo("James"));
		assertThat(customer.getLastName(), equalTo("Alexander"));
		assertThat(customer.getEmail(), equalTo("james.alexander@nice.com"));
		assertThat(customer.getUserName(), equalTo("james.alexander"));
	}
	
	@Test
	public void getAllCustomers_shouldReturnOK() 
	{
		given()
			.spec(spec)
			.port(port)
		.when()
			.get("/customers")
		.then()
			.statusCode(HttpStatus.OK.value()).and()
			.body("size()", is(3));
	}
	
	@Test
	public void createCustomer_shouldReturnCreated() 
	{
		Customer customer = new Customer("Ryan", "Owen", "ryan.owen@nice.com", "ryan.owen");
		String location = given()
			.spec(spec)
			.port(port)
			.request().body(customer)
		.when()
			.post("/customers")
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().header("location");
		
		validateCustomerDetails(customer, location);
	}
	
	@Test
	public void updateCustomer_whenCustomerIdNotExist_shouldReturnNotFound() 
	{
		Customer customer = new Customer("Ryan", "Owen", "ryan.owen@nice.com", "ryan.owen");
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 100)
			.body(customer)
		.when()
			.put("/customers/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void updateCustomer_whenCustomerIdExist_shouldReturnOK() {
		Customer customer = new Customer("Ryan", "Owen", "ryan.owen@nice.com", "ryan.owen");
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 1)
			.body(customer)
		.when()
			.put("/customers/{id}")
		.then()
			.statusCode(HttpStatus.OK.value());
		
		validateCustomerDetails(customer, "/customers/1");
	}
	
	@Test
	public void deleteCustomer_whenCustomerIdNotExist_shouldReturnNotFound() {
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 100)
		.when()
			.delete("/customers/{id}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deleteCustomer_whenCustomerIdExist_shouldReturnNoContent() {
		given()
			.spec(spec)
			.port(port)
			.pathParam("id", 1)
		.when()
			.delete("/customers/{id}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	private void validateCustomerDetails(Customer customer, String url) {
		Customer updatedUser = given()
				.spec(spec)
				.port(port)
			.when()
				.get(url)
			.then()
				.statusCode(HttpStatus.OK.value())
				.extract().as(Customer.class);
			
			assertThat(updatedUser.getFirstName(), equalTo(customer.getFirstName()));
			assertThat(updatedUser.getLastName(), equalTo(customer.getLastName()));
			assertThat(updatedUser.getEmail(), equalTo(customer.getEmail()));
			assertThat(updatedUser.getUserName(), equalTo(customer.getUserName()));
	}
}
