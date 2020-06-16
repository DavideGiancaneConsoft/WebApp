package com.webapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.webapp.bean.Customer;

/**
 * Classe singleton che implementa le operazioni CRUD
 * @author davide.giancane
 *
 */
public class CustomerDAO {
	
	private static CustomerDAO instance;
	private Collection<Customer> cache;
	private boolean isModified;
	
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement statement;
	
	private DBPropertiesManager dbPropertiesManager;
	
	private CustomerDAO() {
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
	 * Ritorna il singleton inizializzando il driver JDBC
	 * @return il singleton della classe
	 */
	public static CustomerDAO getInstance() {
		if(instance == null) {
			instance = new CustomerDAO();
		}
			
		return instance;
	}
	
	/**
	 * Legge tutti i customer presenti nella cache. 
	 * Se la cache � vuota interroga al db, altrimenti ritorna direttamente i customer nella cache
	 * @return una collection con tutti i customer
	 * @throws DaoExceptions se si verificano problemi di interrogazione o di connessione
	 */
	public Collection<Customer> readCustomers() throws DaoExceptions{
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
					int id = resultSet.getInt(ColumnNames.id.toString());
					String city = resultSet.getString(ColumnNames.city.toString());
										
					//Costruisco il customer e lo aggiungo alla collection
					Customer cust = new Customer(first_name, last_name, phone_number, id, city);
					customers.add(cust);
				}

				cache = customers;
			} catch (SQLException e) {
				throw new DaoExceptions(e.getMessage());
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
	
	/**
	 * Inserisce un nuovo customer all'interno del db
	 * @param customer nuovo customer da inserire
	 * @throws DaoExceptions se si verificano problemi di connessione o interrogazione
	 * @throws SQLException
	 */
	public void insertNewCustomer(Customer customer) throws DaoExceptions{
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

			//Aggiorno il flag, indicando che il DB � stato modificato 
			isModified = true;
		} catch (SQLException e) {
			throw new DaoExceptions(e.getMessage());
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception ignored) {}
		}
	}
	
	/**
	 * Elimina un customer dal DB
	 * @param customerID id del customer da eliminare
	 * @throws DaoExceptions 
	 */
	public void deleteCustomer(String customerID) throws DaoExceptions{
		try {
			openNewConnection();
			String query = "DELETE FROM customer WHERE cust_id=?";
		    statement = getNewStatement(query);
			statement.setString(1, customerID);
			
			statement.executeUpdate();
	
			//Aggiorno il flag di modifica
			isModified = true;
		} catch (SQLException e) {
			throw new DaoExceptions(e.getMessage());
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
