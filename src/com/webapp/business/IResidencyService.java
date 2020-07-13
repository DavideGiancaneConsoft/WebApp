package com.webapp.business;

import java.util.Collection;

import javax.ejb.Local;

import com.webapp.bean.City;
import com.webapp.bean.Region;

@Local
public interface IResidencyService {
	public Collection<Region> getRegions();
	public Collection<City> getCitiesByRegion(Integer regionId);
	public City getCityByInitials(String initials);
}
