package com.webapp.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.Customer;
import com.webapp.dao.CustomerDAO;

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
		try {
			//Prendo la lista dei customer
			Collection<Customer> customers = custDao.readCustomers();
			request.setAttribute("customers", customers);
			
			//Forward verso la JSP
			String jspPath = "/customers.jsp";
			getServletContext().getRequestDispatcher(jspPath).forward(request, response);
		} catch (SQLException e) {
			//TODO: PREDISPORRE UNA JSP DI ERRORE
			System.out.println("*** Errore: " + e.getMessage() + " ***");
		}
	}
}
