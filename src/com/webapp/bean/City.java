package com.webapp.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.gson.annotations.Expose;

@Entity
public class City implements Serializable{
	private static final long serialVersionUID = 3958893281486635756L;
	
	@Id
	@Column(length = 2, name = "initials")
	@Expose(serialize = true)
	private String initials;
	
	@Column(length = 20, nullable = false, name = "city_name")
	@Expose(serialize = true)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "belonging_region")
	@Expose(serialize = false)
	private Region region;
	
	public City() {}
	
	public City(String initials, String name, Region region) {
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
	
	public Region getRegion() {
		return region;
	}
	
	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}	
	
	@Override
	public String toString() {
		return "City [name: " + name + ", region: " + region + "]";
	}
}
