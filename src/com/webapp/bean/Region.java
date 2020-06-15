package com.webapp.bean;

import java.io.Serializable;

public class Region implements Serializable {
	private static final long serialVersionUID = 2761256604133768308L;
	
	private int regionID;
	private String regionName;
	
	public Region() {}

	public Region(int regionID, String regionName) {
		this.regionID = regionID;
		this.regionName = regionName;
	}
	
	public int getRegionID() {
		return regionID;
	}
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}	
}
