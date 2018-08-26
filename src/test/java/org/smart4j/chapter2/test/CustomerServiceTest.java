package org.smart4j.chapter2.test;

import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter2.service.CustomerService;

public class CustomerServiceTest {

	private CustomerService customerService;
	
	public CustomerServiceTest() {
		customerService = new CustomerService();
	}
	
	@Before
	public void init() {
		
	}
	
	@Test
	public void getCustomerList() {
		customerService.getCustomerList();
	}
}
