package com.webapp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.webapp.bean.City;
import com.webapp.bean.Region;
import com.webapp.dao.DaoExceptions;
import com.webapp.dao.RegionDAO;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private static final String citiesRequest = "CitiesRequest";
	private RegionDAO regionDao;
	private Gson gson;
	
	@Override
		public void init() throws ServletException {
			super.init();
			regionDao = RegionDAO.getInstance();
			gson = new Gson();
		}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("requestAction");
		String jspPath = null;
		try {
			if(action == null) {
				Collection<Region> regions = regionDao.getRegions();
				request.setAttribute("regions", regions);
				
				//Forward verso la JSP
				jspPath = "/registrationForm.jsp";
				getServletContext().getRequestDispatcher(jspPath).forward(request, response);
			} else if(action.contentEquals(citiesRequest)) {
				int regionID = Integer.valueOf(request.getParameter("regionID"));
				Collection<City> cities = regionDao.getCities(regionID);
				String jsonObject = gson.toJson(cities);
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jsonObject);	
			}
		} catch (DaoExceptions e) {
			//Se si verificano errori predispongo una JSP di errore
			String errorMessage = "Something went wrong! \n " + e.getMessage();
			//log dell'errore
			System.out.println("*** Errore: " + e.getMessage() + " ***");
			
			ServletUtils.forwardError(request, response, getServletContext(), errorMessage);
		}
	}	
}
