package com.webapp.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "cust_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name") 
	private String lastName;
	
	@Column(name = "phone")
	private String phoneNumber;
	
	@JoinColumn(name = "city")
	private String city;
		
	public Customer() {}
	
	public Customer(String firstName, String lastName, String phoneNumber, 
			int id, String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.id = id;
		this.city = city;
	}
	
	public Customer(String firstName,
			String lastName, 
			String phoneNumber,
			String city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
