package com.webapp.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.webapp.bean.City;

/**
 * Servlet implementation class jpaTest
 */
@WebServlet("/jpaTest")
public class jpaTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("WebAppPU");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = null;
	
		try {
			//Creo la query di select che mi ritorna oggetti di tipo Region
			String select = "SELECT initials, city_name FROM (city JOIN region)"
					+ " WHERE (city.region=region.reg_id AND region.reg_id="+1 + ")";
		
			//Query query = em.createNativeQuery(select);
			Query query = em.createNativeQuery(select, City.class); // --> NON FUNZIONA
			
			//Inizio transazione
			transaction = em.getTransaction();
			transaction.begin();
			
			//Eseguo la query di select con ritorno degli oggetti
			@SuppressWarnings("unchecked")
			Collection<City> cities = query.getResultList();
			transaction.commit();	
			
			//ritorno i customers
			for(Object c : cities) {
				System.out.println((City) c);
			}
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione faccio rollback e rilancio DaoException
			if(transaction != null)
				transaction.rollback();
		} finally {
			//infine, a prescindere dall'esito della transazione, chiudo l'entity manager
			em.close();
		}
	}


}
