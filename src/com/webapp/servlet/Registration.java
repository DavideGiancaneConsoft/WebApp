package com.webapp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.webapp.bean.City;
import com.webapp.bean.Region;
import com.webapp.business.ICustomersService;
import com.webapp.business.IResidencyService;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private static final String citiesRequest = "CitiesRequest";
	private Collection<Region> regions;
	private GsonBuilder gsonBuilder;
	
	@EJB
	private ICustomersService customersService;
	
	private IResidencyService residencyService;
	
	@Override
	public void init() throws ServletException {
		super.init();
		gsonBuilder = new GsonBuilder();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("requestAction");
		String jspPath = null;
		try {
			if(action == null) {
				regions = residencyService.getRegions();
				request.setAttribute("regions", regions);
				
				//Forward verso la JSP
				jspPath = "/registrationForm.jsp"; 
				getServletContext().getRequestDispatcher(jspPath).forward(request, response);
			} else if(action.contentEquals(citiesRequest)) {
				int regionID = Integer.valueOf(request.getParameter("regionID"));
				
				Collection<City> cities = residencyService.getCitiesByRegion(regionID);

				Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
				String jsonObject = gson.toJson(cities);
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObject);	
			}
		} catch (Exception e) {
			//Se si verificano errori predispongo una JSP di errore
			String errorMessage = "Something went wrong! \n " + e.getMessage();
			//log dell'errore
			System.err.println("*** Error in 'Servlet Registration.java': " + e.getMessage() + " ***");
			
			ServletUtils.forwardError(request, response, getServletContext(), errorMessage);
		}
	}	
}
