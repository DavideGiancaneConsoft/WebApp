package com.webapp.business;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import com.webapp.bean.City;
import com.webapp.bean.Region;
import com.webapp.dao.DaoException;
import com.webapp.dao.IRegionDAO;
import com.webapp.dao.jpa.RegionDaoJPA;

@Stateless
public class ResidencyEjb implements IResidencyService {
	
	private IRegionDAO dao;
	
	@PostConstruct
	public void initialize() {
		dao = RegionDaoJPA.getInstance();
	}

	@Override
	public Collection<Region> getRegions() {
		Collection<Region> regions = new LinkedList<>();
		try {
			regions = dao.selectRegions();
		} catch (DaoException e) {
			System.err.println("!!! Error: " + e.getMessage());
		}
		
		return regions;
	}

	@Override
	public Collection<City> getCitiesByRegion(Integer regionId) {
		System.out.println("*** entrato in getCitiesByRegion() in EJB ***");
		Collection<City> cities = new LinkedList<>();
		try {
			cities = dao.selectCitiesByRegion(regionId);
		} catch (DaoException e) {
			System.err.println("!!! Error: " + e.getMessage());
		}
		
		return cities;
	}

	@Override
	public City getCityByInitials(String initials) {
		System.out.println("*** entrato in getCityByInitials() in EJB ***");
		City city = null;
		try {
			city = dao.selectCityByInitials(initials);
		} catch (DaoException e) {
			System.err.println("!!! Error: " + e.getMessage());
		}
		
		return city;	
	}

}
