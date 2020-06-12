package com.webapp.bean;

import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String firstName, lastName, phoneNumber, id, region, city;
	
	public Customer() {}
	
	public Customer(String firstName, String lastName, String phoneNumber, 
			String id, String region, String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.id = id;
		this.region = region;
		this.city = city;
	}
	
	public Customer(String firstName,
			String lastName, 
			String phoneNumber,
			String region,
			String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.region = region;
		this.city = city;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
