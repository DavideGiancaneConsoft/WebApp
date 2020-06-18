package com.webapp.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.webapp.bean.Customer;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;

/**
 * Classe singleton DAO che estende <code>ICustomerDAO</code> con supporto JDBC
 * @author davide.giancane
 *
 */
public class CustomerDaoJDBC implements ICustomerDAO {
	
	private static ICustomerDAO instance;
	private Collection<Customer> cache;
	private boolean isModified;
	
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement statement;
	
	private DBPropertiesManager dbPropertiesManager;
	
	private CustomerDaoJDBC() {
		try {
			cache = null;
			isModified = false;
			dbPropertiesManager = DBPropertiesManager.getInstance();
			Class.forName(dbPropertiesManager.getJdbcDriver());
		} catch (ClassNotFoundException e) {
			System.err.println("!!! JDBC Driver not loaded !!!");
		}
	}
	
	/**
	 * Ritorna la singola istanza della classe
	 * @return
	 */
	public static ICustomerDAO getInstance() {
		if(instance == null) {
			instance = new CustomerDaoJDBC();
		}
			
		return instance;
	}
	
	public Collection<Customer> readCustomers() throws DaoException{
		if(cache == null || isModified) {
			try {
				//Preparo la collection
				Collection<Customer> customers = new LinkedList<Customer>();
				//Apro la connessione
				openNewConnection();
				
				//Eseguo la query di select
				String query = "SELECT * FROM customer";
				statement = getNewStatement(query);
				statement.execute();
				
				//Popolo la collection con i vari customer
				resultSet = statement.getResultSet();
				
				while(resultSet.next()) {
					String first_name = resultSet.getString(ColumnNames.firstName.toString());
					String last_name = resultSet.getString(ColumnNames.lastName.toString());
					String phone_number = resultSet.getString(ColumnNames.phoneNumber.toString());
					Integer id = resultSet.getInt(ColumnNames.id.toString());
					String city = resultSet.getString(ColumnNames.city.toString());
										
					//Costruisco il customer e lo aggiungo alla collection
					Customer cust = new Customer(first_name, last_name, phone_number, id, city);
					customers.add(cust);
				}

				cache = customers;
			} catch (SQLException e) {
				throw new DaoException(e.getMessage());
			} finally {
				try {
					resultSet.close();
					statement.close();
					connection.close();
				} catch (Exception ignored) {}
			}
		}
	
		return cache;	
	}
	
	public void insertCustomer(Customer customer) throws DaoException{
		try {
			openNewConnection();
			
			String query = "INSERT INTO customer (first_name, last_name, phone, city)"
					+ " VALUES (?, ?, ?, ?)";
			PreparedStatement statement = getNewStatement(query);
			statement.setString(1, customer.getFirstName());
			statement.setString(2, customer.getLastName());
			statement.setString(3, customer.getPhoneNumber());
			statement.setString(4, customer.getCity());
			
			statement.executeUpdate();

			//Aggiorno il flag, indicando che il DB è stato modificato 
			isModified = true;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception ignored) {}
		}
	}
	

	public void deleteCustomer(Integer customerID) throws DaoException{
		try {
			openNewConnection();
			String query = "DELETE FROM customer WHERE cust_id=?";
		    statement = getNewStatement(query);
			statement.setInt(1, customerID);
			
			statement.executeUpdate();
	
			//Aggiorno il flag di modifica
			isModified = true;
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception ignored) {}
		}
	}
	
	/**
	 * Apre una nuova connessione
	 * @throws SQLException 
	 */
	private void openNewConnection() throws SQLException {
		String DB_URI = dbPropertiesManager.getDbURI();
		String DB_USER = dbPropertiesManager.getUser();
		String DB_PSW = dbPropertiesManager.getPsw();
		connection = DriverManager.getConnection(DB_URI, DB_USER, DB_PSW);
	}
	
	/**
	 * Crea uno statement a partire da una query
	 * @param query interrogazione da effettuare
	 * @return un PreparedStatement per la query
	 * @throws SQLException
	 */
	private PreparedStatement getNewStatement(String query) throws SQLException {
		PreparedStatement statement = 
				connection.prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return statement;
	}

	/**
	 * Enum utility per considerare i nomi delle colonne del db come costanti
	 * @author davide.giancane
	 *
	 */
	private enum ColumnNames{
		firstName("first_name"),
		lastName("last_name"), 
		phoneNumber("phone"),
		id("cust_id"),
		city("city");
		
		private String columnName;
		
		private ColumnNames(String name) {
			this.columnName = name;
		}
		
		@Override
		public String toString() {
			return columnName;
		}	
	}
}
