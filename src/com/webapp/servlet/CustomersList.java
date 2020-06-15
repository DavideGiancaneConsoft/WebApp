package com.webapp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.Customer;
import com.webapp.dao.CustomerDAO;
import com.webapp.dao.DaoExceptions;

/**
 * Servlet che gestisce la richiesta del client di ricevere 
 * la lista dei customers
 */
@WebServlet("/customersList")
public class CustomersList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerDAO custDao;
       
    public CustomersList() {super();}
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	custDao = CustomerDAO.getInstance();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jspPath = null;
		try {
			//Prendo la lista dei customer
			Collection<Customer> customers = custDao.readCustomers();
			request.setAttribute("customers", customers);
			
			//Forward verso la JSP
			jspPath = "/customers.jsp";
			getServletContext().getRequestDispatcher(jspPath).forward(request, response);
		
		} catch (DaoExceptions e) {
			//Se si verificano errori predispongo una JSP di errore
			String errorMessage = "Something went wrong with the database. Try again!";
			
			ServletUtils.forwardInternalServerError(request, response, getServletContext(), errorMessage);
			
			//log dell'errore
			System.err.println("*** Errore: " + e.getMessage() + " ***");
		}
	}
}
