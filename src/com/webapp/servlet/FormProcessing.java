package com.webapp.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.City;
import com.webapp.bean.Customer;
import com.webapp.business.ICustomersService;
import com.webapp.business.IResidencyService;

/**
 * Servlet implementation class FormProcessing
 */
@WebServlet("/formProcessing")
public class FormProcessing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ICustomersService customersService;
	
	@EJB
	private IResidencyService residencyService;
   
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Prendo i parametri dei campi della form
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String phoneNumber = request.getParameter("phoneNumber");
		String cityInitials = request.getParameter("city");
		
		String jspPath = null;
		
		try {
			//prendo la città
			City theCity = residencyService.getCityByInitials(cityInitials);
			
			//Istanzio un customer
			Customer newCustomer = new Customer(firstName, lastName, phoneNumber, theCity);
			
			//Inserisco l'oggetto nel DB
			customersService.addCustomer(newCustomer);
			
			//vado su una pagina di conferma della registrazione
			jspPath = "/successfulRegistration.jsp";
			getServletContext().getRequestDispatcher(jspPath).forward(request, response);
		
		} catch (Exception e) {
			//Se si verificano errori predispongo una JSP di errore
			String errorMessage = "Something went wrong. Try again!";
			//log dell'errore
			System.out.println("*** Errore: " + e.getMessage() + " ***");
			ServletUtils.forwardError(request, response, getServletContext(), errorMessage);
		}
	}

}
