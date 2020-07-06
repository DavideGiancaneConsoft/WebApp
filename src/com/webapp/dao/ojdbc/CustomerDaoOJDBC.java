package com.webapp.dao.ojdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import com.webapp.bean.Customer;
import com.webapp.dao.DBPropertiesManager;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;

public class CustomerDaoOJDBC implements ICustomerDAO{
	
	private static CustomerDaoOJDBC instance;
	
	private Connection connection;
	
	private DBPropertiesManager dbPropertiesManager;

	private CustomerDaoOJDBC() {}
	
	public static CustomerDaoOJDBC getInstance() {
		if(instance == null) {
			instance = new CustomerDaoOJDBC();
			instance.dbPropertiesManager = DBPropertiesManager.getInstance();
		}
		return instance;
	}
	
	@Override
	public Collection<Customer> readCustomers() throws DaoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertCustomer(Customer c) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCustomer(int id) throws DaoException {
		// TODO Auto-generated method stub
		
	}
	
	public void run() throws SQLException {
		connection = DriverManager.getConnection(dbPropertiesManager.getOracleDbUri(),
				dbPropertiesManager.getOracleDbUser(), dbPropertiesManager.getOracleDbPsw());
		if(connection != null)
			System.out.println("Connected to db");
		else
			System.out.println("Fail to make connection");
	}

	
	public static void main(String[] args) {
		CustomerDaoOJDBC dao = CustomerDaoOJDBC.getInstance();
		try {
			dao.run();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			e.printStackTrace();
		}
	}

}
