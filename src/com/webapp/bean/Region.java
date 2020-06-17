package com.webapp.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "reg_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int regionID;
	
	@Column(name = "region_name")
	private String regionName;
	
	public Region() {}

	public Region(int regionID, String regionName) {
		this.regionID = regionID;
		this.regionName = regionName;
	}
	
	public int getRegionID() {
		return regionID;
	}
	
	public String getRegionName() {
		return regionName;
	}
	
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}	
	
	@Override
	public String toString() {
		return "Region [ID: " + regionID + ", Name: " + regionName + "]";
	}
}
