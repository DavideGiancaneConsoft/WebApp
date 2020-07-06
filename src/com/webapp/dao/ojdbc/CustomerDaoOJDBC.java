package com.webapp.dao.ojdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.webapp.bean.Customer;
import com.webapp.dao.DBPropertiesManager;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;

import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;

public class CustomerDaoOJDBC implements ICustomerDAO{
	
	private static CustomerDaoOJDBC instance;
	private Connection connection;
	private DBPropertiesManager dbPropertiesManager;
	private CallableStatement stmt;

	private CustomerDaoOJDBC() {}
	
	public static CustomerDaoOJDBC getInstance() {
		if(instance == null) {
			instance = new CustomerDaoOJDBC();
			instance.dbPropertiesManager = DBPropertiesManager.getInstance();
		}
		return instance;
	}
	
	/**
	 * Richiama la procedura dichiarata nel server di Oracle DB
	 * che ritorna tutti i customer registrati
	 */
	@Override
	public Collection<Customer> readCustomers() throws DaoException {
		ResultSet rs = null;
		try {
			//Apro la connessione e preparo lo statement che richiama la procedura "get_all_customers"
			openNewConnection();
			stmt = connection.prepareCall("{call customer_package.get_all_customers(?)}");
			
			//Imposto il tipo del parametro della procedura (che è di ritorno), ovvero un CURSORE
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			
			//Eseguo lo statement
			stmt.execute();
			
			//Prendo il cursore e lo casto a result set per poterlo analizzare lato java
			rs = (ResultSet) stmt.getObject(1);
			
			while(rs.next()) {
				System.out.println("Customer " + rs.getInt("cust_id"));
				System.out.println("- Name: " + rs.getString("first_name"));
				System.out.println("- Last name: " + rs.getString("last_name"));
				System.out.println("- Phone: " + rs.getString("phone"));
				System.out.println("- City: " + rs.getString("city"));
			}
			
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				System.err.println("*** Error in CustomerDaoOJDBC -> readCustomers(): " +
						e.getMessage() + " ***");
			} catch (NullPointerException ignored) {}
		}
		return null;
	}

	/**
	 * Richiama la procedura dichiarata nel server di Oracle DB
	 * che inserisce un nuovo customer nel DB
	 */
	@Override
	public void insertCustomer(Customer c) throws DaoException {
		try {
			//Apro la connessione e preparo lo statement
			openNewConnection();
			stmt = connection.prepareCall("{call customer_package.add_customer(?,?,?,?,?)}");
			//stmt.setEscapeProcessing(false);
			
			//I parametri da passare sono 5 (4 di customer + 1 di output)
			stmt.setString(1, c.getFirstName());
			stmt.setString(2, c.getLastName());
			stmt.setString(3, c.getPhoneNumber());
			stmt.setString(4, c.getCity());
			
			//parametro di output
			stmt.registerOutParameter(5, OracleType.VARCHAR2);
			
			//eseguo la procedura
			stmt.execute();
			
			//Prendo la stringa che rappresenta l'esito della procedura e la stampo
			String result = stmt.getString(5);
			System.err.println("Customer record save: success = " + result);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				System.err.println("*** Error in CustomerDaoOJDBC -> insertCustomer(): " +
						e.getMessage() + " ***");
			} catch (NullPointerException ignored) {}
		}
	}

	/**
	 * Richiama la procedura dichiarata nel server di Oracle DB
	 * che elimina un customer esistente dato un DB
	 */
	@Override
	public void deleteCustomer(int id) throws DaoException {
		// TODO Auto-generated method stub
		
	}
	
	private void openNewConnection() throws SQLException {
		String oracleDbUri = dbPropertiesManager.getOracleDbUri();
		String oracleDbUser = dbPropertiesManager.getOracleDbUser();
		String oracleDbPsw =  dbPropertiesManager.getOracleDbPsw();
		
		connection = DriverManager.getConnection(oracleDbUri, oracleDbUser, oracleDbPsw);
	}
	
	public static void main(String[] args) throws DaoException {
		CustomerDaoOJDBC dao = CustomerDaoOJDBC.getInstance();
		Customer c = new Customer("Pinco", "Pallino", "484820", "MI");
		dao.insertCustomer(c);
	}
}
