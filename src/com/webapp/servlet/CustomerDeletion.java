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
		String jspPath = null;
		try {
			//Elimino il customer dal DB
			custDao.deleteCustomer(customerID);
			
			//Ricarico la lista dei customer aggiornata
			Collection<Customer> customers = custDao.readCustomers();
			request.setAttribute("customers", customers);
			
			//-----------------------------------------
			//FIXME: *** provare con chiamata AJAX con jquery ***
			//-----------------------------------------
			
			//Passo la lista aggiornata alla JSP
		    jspPath = "/customers.jsp";
			getServletContext().getRequestDispatcher(jspPath).forward(request, response);
		} catch (DaoExceptions e) {
			//Se si verificano errori predispongo una JSP di errore
			String errorMessage = "Something went wrong with the database. Try again!";
			ServletUtils.forwardInternalServerError(request, response, getServletContext(), errorMessage);
			
			//log dell'errore
			System.out.println("*** Errore: " + e.getMessage() + " ***");
		}
	}
}
