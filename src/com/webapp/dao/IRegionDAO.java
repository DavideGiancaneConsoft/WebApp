package com.webapp.dao;

import java.util.Collection;

import com.webapp.bean.City;
import com.webapp.bean.Region;

public interface IRegionDAO {

	/**
	 * Ritorna tutte le regioni presenti nel DB
	 * @return le regioni della tabella <i>Regions</i>
	 * @throws DaoException se si verificano errori di comunicazione col DB
	 */
	public Collection<Region> getRegions() throws DaoException;
	
	/**
	 * Ritorna le citt� afferenti ad una specifica regione
	 * @param regionID identificativo della regione
	 * @return le citt� della regione <code>id</code>
	 * @throws DaoException
	 */
	public Collection<City> getCities(int regionID) throws DaoException;
}
