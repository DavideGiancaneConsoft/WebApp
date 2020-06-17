package com.webapp.dao.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.webapp.bean.City;
import com.webapp.bean.Customer;
import com.webapp.bean.Region;
import com.webapp.dao.DaoException;
import com.webapp.dao.IRegionDAO;

public class RegionDaoJPA implements IRegionDAO {
	private static IRegionDAO instance;
	private EntityManagerFactory emf;
	
	private RegionDaoJPA() {
		emf = Persistence.createEntityManagerFactory("WebAppPU");
	}
	
	public IRegionDAO getInstance() {
		if(instance == null)
			instance = new RegionDaoJPA();
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Region> getRegions() throws DaoException {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = null;
	
		try {
			//Creo la query di select che mi ritorna oggetti di tipo Region
			Query query = em.createNativeQuery("SELECT * FROM region", Region.class);
			
			//Inizio transazione
			transaction = em.getTransaction();
			transaction.begin();
			
			//Eseguo la query di select con ritorno degli oggetti
			Collection<Region> regions = query.getResultList();
			transaction.commit();	
			
			//ritorno i customers
			return regions;
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione faccio rollback e rilancio DaoException
			if(transaction != null)
				transaction.rollback();
			throw new DaoException(e.getMessage());
		} finally {
			//infine, a prescindere dall'esito della transazione, chiudo l'entity manager
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<City> getCities(int regionID) throws DaoException {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = null;
	
		try {
			//Creo la query di select che mi ritorna oggetti di tipo Region
			String select = "SELECT initials, city_name FROM (city JOIN region)"
					+ " WHERE (city.region=region.reg_id AND reg.id="+regionID;
			
			Query query = em.createNativeQuery(select, City.class);
			
			//Inizio transazione
			transaction = em.getTransaction();
			transaction.begin();
			
			//Eseguo la query di select con ritorno degli oggetti
			Collection<City> cities = query.getResultList();
			transaction.commit();	
			
			//ritorno i customers
			return cities;
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione faccio rollback e rilancio DaoException
			if(transaction != null)
				transaction.rollback();
			throw new DaoException(e.getMessage());
		} finally {
			//infine, a prescindere dall'esito della transazione, chiudo l'entity manager
			em.close();
		}
	}

}
