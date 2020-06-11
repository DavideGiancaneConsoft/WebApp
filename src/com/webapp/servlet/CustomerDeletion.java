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
 * Servlet implementation class CustomerDeletion
 */
@WebServlet("/customerDeletion")
public class CustomerDeletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CustomerDAO custDao;
   
    public CustomerDeletion() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	custDao = CustomerDAO.getInstance();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prendo l'ID del customer da eliminare
		String customerID = request.getParameter("customerID");
		
		try {
			//Elimino il customer dal DB
			custDao.deleteCustomer(customerID);
			
			//Ricarico la lista dei customer aggiornata
			Collection<Customer> customers = custDao.readCustomers();
			request.setAttribute("customers", customers);
			
			//Passo la lista aggiornata alla JSP
			//TODO: *** provare con chiamata AJAX ***
			//TODO: PROVARE AD USARE JQUERY
			String jspPath = "/customers.jsp";
			getServletContext().getRequestDispatcher(jspPath).forward(request, response);
		} catch (SQLException e) {
			System.out.println("*** Errore: " + e.getMessage() + " ***");
		}
	}
}
