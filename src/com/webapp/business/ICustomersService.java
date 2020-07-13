package com.webapp.business;

import java.util.Collection;

import javax.ejb.Local;

import com.webapp.bean.Customer;

/**
 * Interfaccia dei servizi che le implementazioni EJB
 * devono fornire
 * @author davide.giancane
 *
 */
@Local
public interface ICustomersService {
	public Collection<Customer> getCustomers();
	public void addCustomer(Customer customer);
	public void deleteCustomer(Integer custId);
}
