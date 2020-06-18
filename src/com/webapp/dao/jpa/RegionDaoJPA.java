package com.webapp.dao.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.webapp.bean.City;
import com.webapp.bean.Region;
import com.webapp.dao.DaoException;
import com.webapp.dao.IRegionDAO;

public class RegionDaoJPA implements IRegionDAO {
	private static IRegionDAO instance;
	private EntityManagerFactory emf;
	
	private RegionDaoJPA() {
		emf = Persistence.createEntityManagerFactory("WebAppPU");
	}
	
	public static IRegionDAO getInstance() {
		if(instance == null)
			instance = new RegionDaoJPA();
		return instance;
	}

	@Override
	public Collection<Region> selectRegions() throws DaoException {
		EntityManager em = emf.createEntityManager();	
		try {
			//Creo la query di select che mi ritorna oggetti di tipo Region
			String regionsSelect = "SELECT r FROM Region r";
			
			//Creo la TypedQuery usando JPQL
			TypedQuery<Region> query = em.createQuery(regionsSelect, Region.class);

			//Eseguo la query di select con ritorno degli oggetti
			Collection<Region> cities = query.getResultList();
			
			//ritorno i customers
			return cities;
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione rilancio una DaoException
			throw new DaoException(e.getMessage());
		} finally {
			//infine, a prescindere dall'esito della transazione, chiudo l'entity manager
			em.close();
		}
	}

	@Override
	public Collection<City> selectCitiesByRegion(Integer regionID) throws DaoException {
		EntityManager em = emf.createEntityManager();	
		try {
			//Creo la query di select che mi ritorna oggetti di tipo City
			String citySelect = "SELECT c FROM Region r JOIN r.cities c WHERE (c.region=r.regionID AND r.regionID= :id)";
			
			//Creo la TypedQuery usando JPQL
			TypedQuery<City> query = em.createQuery(citySelect, City.class);
			query.setParameter("id", regionID);
		
			//Eseguo la query di select con ritorno degli oggetti
			Collection<City> cities = query.getResultList();
			
			//ritorno i customers
			return cities;
		} catch (Exception e) {
			//Se si verifica una qualsiasi eccezione rilancio una DaoException
			throw new DaoException(e.getMessage());
		} finally {
			//infine, a prescindere dall'esito della transazione, chiudo l'entity manager
			em.close();
		}
	}

	@Override
	public City selectCityByInitials(String initials) throws DaoException {
		EntityManager em = emf.createEntityManager();	
		try {
			return em.find(City.class, initials);
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		} finally {
			em.close();
		}
	}

}
