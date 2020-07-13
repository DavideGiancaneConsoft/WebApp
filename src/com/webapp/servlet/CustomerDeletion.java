package com.webapp.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.business.ICustomersService;

/**
 * Servlet implementation class CustomerDeletion
 */
@WebServlet("/customerDeletion")
public class CustomerDeletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ICustomersService customersService;
    
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prendo l'ID del customer da eliminare
		String customerID = request.getParameter("customerID");
		try {
			//Elimino il customer dal DB
			customersService.deleteCustomer(Integer.valueOf(customerID));
		} catch (Exception e) {	
			//log dell'errore
			System.err.println("*** Error in Servlet CustomerDeletion.java: " + e.getMessage() + " ***");
		}
	}
}
