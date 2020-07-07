package com.webapp.dao.ojdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.webapp.bean.Customer;
import com.webapp.dao.DBPropertiesManager;
import com.webapp.dao.DBPropertiesManager.DBType;
import com.webapp.dao.DaoException;
import com.webapp.dao.ICustomerDAO;

import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;

public class CustomerDaoOJDBC implements ICustomerDAO{
	
	private static ICustomerDAO instance;
	private Connection connection;
	private DBPropertiesManager dbPropertiesManager;
	private CallableStatement stmt;

	private CustomerDaoOJDBC() {
		try {
			dbPropertiesManager = DBPropertiesManager.getInstance();
			Class.forName(dbPropertiesManager.getDatabaseDriver(DBType.OracleDB));
		} catch (Exception e) {
			System.err.println("!!! Error: " + e.getMessage() + " !!!");
		}
	}
	
	public static ICustomerDAO getInstance() {
		if(instance == null)
			instance = new CustomerDaoOJDBC();
		return instance;
	}
	
	/**
	 * Richiama la procedura dichiarata nel server di Oracle DB
	 * che ritorna tutti i customer registrati
	 */
	@Override
	public Collection<Customer> readCustomers() throws DaoException {
		ResultSet rs = null;
		Collection<Customer> customers = null;
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
			customers = new LinkedList<>();
			
			while(rs.next()) {
				Customer c = new Customer();
				c.setId(rs.getInt("cust_id"));
				c.setFirstName(rs.getString("first_name"));
				c.setLastName(rs.getString("last_name"));
				c.setPhoneNumber(rs.getString("phone"));
				c.setCity(rs.getString("city"));
				
				customers.add(c);
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
		return customers;
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
			System.out.println("Customer record save: success = " + result);
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
		try {
			//Apro la connessione e preparo lo statement
			openNewConnection();
			stmt = connection.prepareCall("{call customer_package.delete_customer(?)}");
			
			//Passo l'id come parametro delle procedura
			stmt.setInt(1, id);
		
			//eseguo la procedura
			stmt.execute();	
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				System.err.println("*** Error in CustomerDaoOJDBC -> deleteCustomer(..): " +
						e.getMessage() + " ***");
			} catch (NullPointerException ignored) {}
		}		
	}
	
	/**
	 * Apre una nuova connessione col db
	 * @throws SQLException
	 */
	private void openNewConnection() throws SQLException {
		String oracleDbUri = dbPropertiesManager.getOracleDbUri();
		String oracleDbUser = dbPropertiesManager.getOracleDbUser();
		String oracleDbPsw =  dbPropertiesManager.getOracleDbPsw();
		
		connection = DriverManager.getConnection(oracleDbUri, oracleDbUser, oracleDbPsw);
	}
}
