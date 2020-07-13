package com.webapp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.Customer;
import com.webapp.business.ICustomersService;

/**
 * Servlet che gestisce la richiesta del client di ricevere 
 * la lista dei customers
 */
@WebServlet("/customersList")
public class CustomersList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ICustomersService customersService;
       
    public CustomersList() {super();}
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jspPath = null;

		Collection<Customer> customers = customersService.getCustomers();
		request.setAttribute("customers", customers);
		
		//Forward verso la JSP
		jspPath = "/customers.jsp";
		getServletContext().getRequestDispatcher(jspPath).forward(request, response);
	}
}
