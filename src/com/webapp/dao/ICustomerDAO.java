package com.webapp.dao;

import java.util.Collection;

import com.webapp.bean.Customer;

/**
 * Classe astratta che definisce il DAO di accesso ai dati del customer.
 * @author davide.giancane
 *
 */
public interface ICustomerDAO {
	
	/**
	 * Legge tutti i customers dal db
	 * @return i customers presenti nella tabella <i>Customer</i>
	 * @throws DaoException se si verificano errori durante la comunicazione col DB
	 */
	public Collection<Customer> readCustomers() throws DaoException;
	
	/**
	 * Inserisce un nuovo customer all'interno della tabella <i>Customer</i>
	 * @param c nuovo customer da inserire
	 * @throws DaoException se si verificano errori durante la comunicazione col DB
	 */
	public void insertCustomer(Customer c) throws DaoException;
	
	/**
	 * Rimuove un customer dal DB
	 * @param id identificativo del customer
	 * @throws DaoException se si verificano errori durante la comunicazione col DB
	 */
	public void deleteCustomer(String id) throws DaoException;
}
