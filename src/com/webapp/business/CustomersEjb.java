package com.webapp.business;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import com.webapp.bean.Customer;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;
import com.webapp.dao.jpa.CustomerDaoJPA;

@Stateless
public class CustomersEjb implements ICustomersService{
	
	private ICustomerDAO dao;
	
	@PostConstruct
	public void initalize() {
		dao = CustomerDaoJPA.getInstance();
	}

	@Override
	public Collection<Customer> getCustomers() {
		System.out.println("**** entrato in getCustomers() di EJB ****");
		Collection<Customer> customers = new LinkedList<>();
		try {
			customers = dao.selectAllCustomers();
		} catch (DaoException e) {
			System.err.println("!!! Error: " + e.getMessage());
		}
		return customers;
	}

	@Override
	public void addCustomer(Customer customer) {
		System.out.println("**** entrato in addCustomer() di EJB");
		try {
			dao.insertCustomer(customer);
		} catch (DaoException e) {
			System.err.println("!!! Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteCustomer(Integer custId) {
		System.out.println("**** entrato in deleteCustomer() di EJB");
		try {
			dao.deleteCustomer(custId);
		} catch (DaoException e) {
			System.err.println("!!! Error: " + e.getMessage());
		}
	}

	public ICustomerDAO getDao() {
		return dao;
	}

	public void setDao(ICustomerDAO dao) {
		this.dao = dao;
	}
}
