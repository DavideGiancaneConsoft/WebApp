package com.webapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;
import com.webapp.dao.jdbc.CustomerDaoJDBC;
import com.webapp.dao.jpa.CustomerDaoJPA;

/**
 * Servlet implementation class CustomerDeletion
 */
@WebServlet("/customerDeletion")
public class CustomerDeletion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ICustomerDAO custDao;
    
    @Override
    public void init() throws ServletException {
    	super.init();
    	custDao = CustomerDaoJDBC.getInstance();
    }
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prendo l'ID del customer da eliminare
		String customerID = request.getParameter("customerID");
		try {
			//Elimino il customer dal DB
			custDao.deleteCustomer(Integer.valueOf(customerID));
		} catch (DaoException e) {	
			//log dell'errore
			System.err.println("*** Error in Servlet CustomerDeletion.java: " + e.getMessage() + " ***");
		}
	}
}
