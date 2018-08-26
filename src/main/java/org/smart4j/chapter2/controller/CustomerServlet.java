package org.smart4j.chapter2.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet{

	private CustomerService customerService;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("---------customer--------");
		List<Customer> list = customerService.getCustomerList();
		req.setAttribute("list", list);
		System.out.println("---------method---");
		req.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(req, resp);
	}


	@Override
	public void init() throws ServletException {
		customerService = new CustomerService();
	}

}
