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
	private Connection connection;
	private Collection<Customer> cache;
	private boolean isModified;
	
	//Costanti per l'accesso al db
	private static final String DB_URI = "jdbc:mysql://localhost/test?serverTimezone=UTC";
	private static final String DB_USER = "root";
	private static final String DB_PSW = "consoft";
	
	private CustomerDAO() {}
	
	/**
	 * Ritorna il singleton inizializzando il driver JDBC
	 * @return il singleton della classe
	 */
	public static CustomerDAO getInstance() {
		if(instance == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				instance = new CustomerDAO();
				instance.cache = null;
				instance.isModified = false;
			} catch (ClassNotFoundException e) {
				System.err.println("!!! Driver non caricato !!!");
			}
		}
			
		return instance;
	}
	
	/**
	 * Legge tutti i customer presenti nella cache. 
	 * Se la cache è vuota interroga al db, altrimenti ritorna direttamente i customer nella cache
	 * @return una collection con tutti i customer
	 * @throws SQLException se si verificando problemi di connessione o interrogazione
	 */
	public Collection<Customer> readCustomers() throws SQLException{
		if(cache == null || isModified) {
			//Preparo la collection
			Collection<Customer> customers = new LinkedList<Customer>();
			
			//Apro la connessione
			openNewConnection();
			
			//Eseguo la query di select
			String query = "SELECT * FROM customer";
			PreparedStatement statement = getNewStatement(query);
			statement.execute();
			
			//Popolo la collection con i vari customer
			ResultSet resultSet = statement.getResultSet();
			
			while(resultSet.next()) {
				String first_name = resultSet.getString(ColumnNames.firstName.toString());
				String last_name = resultSet.getString(ColumnNames.lastName.toString());
				String phone_number = resultSet.getString(ColumnNames.phoneNumber.toString());
				String id = resultSet.getString(ColumnNames.id.toString());
				Customer cust = new Customer(first_name, last_name, phone_number, id);
				
				customers.add(cust);
			}
			
			//TODO: GESTIRE L'ECCEZIONE QUI DENTRO CHIUDENDO LA CONNESSIONE ANZICHè RILANCIARLA
			//USARE UNA FINALLY
			
			//Chiudo la connessione 
			connection.close();
			
			//Cacho i customers
			cache = customers;
		}
	
		return cache;	
	}
	
	/**
	 * Inserisce un nuovo customer all'interno del db
	 * @param customer nuovo customer da inserire
	 * @throws SQLException
	 */
	public void insertNewCustomer(Customer customer) throws SQLException {
		openNewConnection();
		
		String query = "INSERT INTO customer (first_name, last_name, phone) VALUES (?, ?, ?)";
		PreparedStatement statement = getNewStatement(query);
		statement.setString(1, customer.getFirstName());
		statement.setString(2, customer.getLastName());
		statement.setString(3, customer.getPhoneNumber());

		statement.executeUpdate();
		
		connection.close();
		
		//Aggiorno il flag, indicando che il DB è stato modificato 
		isModified = true;
	}
	
	/**
	 * Elimina un customer dal DB
	 * @param customerID id del customer da eliminare
	 * @throws SQLException 
	 */
	public void deleteCustomer(String customerID) throws SQLException {
		openNewConnection();
		
		String query = "DELETE FROM customer WHERE cust_id=?";
		PreparedStatement statement = getNewStatement(query);
		statement.setString(1, customerID);
		
		statement.executeUpdate();
		
		connection.close();
		
		//Aggiorno il flag di modifica
		isModified = true;
	}
	
	/**
	 * Apre una nuova connessione
	 * @throws SQLException 
	 */
	private void openNewConnection() throws SQLException {
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
		id("cust_id");
		
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
