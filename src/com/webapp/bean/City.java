package com.webapp.bean;

import java.io.Serializable;

public class City implements Serializable{
	private static final long serialVersionUID = 3958893281486635756L;
	
	private char[] initials;
	private String name;
	private int region;
	
	public City() {
		this.initials = new char[2];
	}
	
	public City(char[] initials, String name, int region) {
		this.initials  = initials;
		this.name = name;
		this.region = region;
	}
	
	public char[] getInitials() {
		return initials;
	}
	public void setInitials(char[] initials) {
		this.initials = initials;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}	
}
