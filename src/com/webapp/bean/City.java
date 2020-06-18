package com.webapp.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class City implements Serializable{
	private static final long serialVersionUID = 3958893281486635756L;
	
	@Id
	private String initials;
	
	@Column(name = "city_name")
	private String name;
	
	@Column(name = "region")
	private Integer region;
	
	public City() {}
	
	public City(String initials, String name, Integer region) {
		this.initials  = initials;
		this.name = name;
		this.region = region;
	}
	
	public String getInitials() {
		return initials;
	}
	
	public String getName() {
		return name;
	}
	
	public int getRegion() {
		return region;
	}
	
	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRegion(Integer region) {
		this.region = region;
	}	
}
