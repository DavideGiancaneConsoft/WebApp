package com.webapp.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.Region;

/**
 * Servlet implementation class JpaTestServlet
 */
@WebServlet("/jpaTestServlet")
public class JpaTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("WebAppPU");
		EntityManager em = emf.createEntityManager();
		
		Region r = em.find(Region.class, 2);
		
		emf.close();
		
		System.out.println(r);
	}

}
