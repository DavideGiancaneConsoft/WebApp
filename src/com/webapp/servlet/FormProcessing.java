package com.webapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.Customer;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;
import com.webapp.dao.jdbc.CustomerDaoJDBC;

/**
 * Servlet implementation class FormProcessing
 */
@WebServlet("/formProcessing")
public class FormProcessing extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private ICustomerDAO custDao;
   

    /**
     * Inizializzo il DAO al primo caricamento della Servlet
     */
    @Override
    public void init() throws ServletException {
    	super.init();
    	custDao = CustomerDaoJDBC.getInstance();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prendo i parametri dei campi della form
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String phoneNumber = request.getParameter("phoneNumber");
		String city = request.getParameter("city");
		
		//Istanzio un customer
		Customer newCustomer = new Customer(firstName, lastName, phoneNumber, city);
		
		String jspPath = null;
		
		try {
			//Inserisco l'oggetto nel DB
			custDao.insertCustomer(newCustomer);
			
			//vado su una pagina di conferma della registrazione
			jspPath = "/successfulRegistration.jsp";
			getServletContext().getRequestDispatcher(jspPath).forward(request, response);
		
		} catch (DaoException e) {
			//Se si verificano errori predispongo una JSP di errore
			String errorMessage = "Something went wrong with the database. Try again!";
			//log dell'errore
			System.out.println("*** Errore: " + e.getMessage() + " ***");
			ServletUtils.forwardError(request, response, getServletContext(), errorMessage);
		}
	}

}
