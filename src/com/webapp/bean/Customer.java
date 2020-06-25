package com.webapp.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(length = 2, name = "cust_id")
	@GeneratedValue(strategy = GenerationType.AUTO) //questa dipende dal DB sottostante
	private Integer id;
	
	@Column(length = 20, nullable = false, name = "first_name")
	private String firstName;
	
	@Column(length = 20, nullable = false, name = "last_name") 
	private String lastName;
	
	@Column(length = 20, nullable = false, name = "phone")
	private String phoneNumber;
	
	@ManyToOne
	@JoinColumn(name = "city")
	private City city;
		
	public Customer() {}
	
	public Customer(String firstName, String lastName, String phoneNumber, 
			Integer id, City city) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.id = id;
		this.city = city;
	}
	
	public Customer(String firstName,
			String lastName, 
			String phoneNumber,
			City city) {
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
	
	public City getCity() {
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

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return "Customer [" 
				+ firstName +
				" " 
				+ lastName +
				"]";
	}
}
