package com.webapp.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "reg_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer regionID;
	
	@Column(length = 20, name = "region_name")
	private String regionName;
	
	@OneToMany(mappedBy = "region", fetch = FetchType.EAGER)
	private Collection<City> cities;
	
	public Region() {
		cities = new LinkedList<>();
	}

	public Region(Integer regionID, String regionName) {
		this.regionID = regionID;
		this.regionName = regionName;
	}
	
	public Integer getRegionID() {
		return regionID;
	}
	
	public String getRegionName() {
		return regionName;
	}
	
	public void setRegionID(Integer regionID) {
		this.regionID = regionID;
	}
	
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}	
	
	public Collection<City> getCities() {
		return cities;
	}

	public void setCities(Collection<City> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "Region [ID: " + regionID + ", Name: " + regionName + "]";
	}
}
