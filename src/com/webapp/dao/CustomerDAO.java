package com.webapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	//Costanti per l'accesso al db
	private static final String DB_URI = "jdbc:mysql://localhost/test";
	private static final String DB_USER = "root";
	private static final String DB_PSW = "consoft";
	
	private CustomerDAO() {}
	
	/**
	 * Ritorna il singleton inizializzando il driver JDBC
	 * @return il singleton della classe
	 */
	public static CustomerDAO getInstance() {
		if(instance == null) {
			instance = new CustomerDAO();
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.err.println("!!! Driver non caricato !!!");
			}
		}
			
		return instance;
	}
	
	/**
	 * Legge tutti i customer presenti nella tabella del DB
	 * @return una collection con tutti i customer
	 * @throws SQLException se si verificando problemi di connessione o interrogazione
	 */
	public Collection<Customer> readCustomers() throws SQLException{
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
			Customer cust = new Customer(first_name, last_name, phone_number);
			
			customers.add(cust);
		}
		
		//Chiudo la connessione
		connection.close();
		
		return customers;	
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
		firstName("first_name"), lastName("last_name"), phoneNumber("phone");
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
