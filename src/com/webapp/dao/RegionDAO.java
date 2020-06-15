package com.webapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.webapp.bean.City;
import com.webapp.bean.Region;

public class RegionDAO {
	private static RegionDAO instance;
	private Collection<Region> cache;
	
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement statement;
	
	//Costanti per l'accesso al db
	private static final String DB_URI = "jdbc:mysql://localhost/test?serverTimezone=UTC";
	private static final String DB_USER = "root";
	private static final String DB_PSW = "consoft";
	
	private RegionDAO() {}
	
	/**
	 * Ritorna il singleton inizializzando il driver JDBC
	 * @return il singleton della classe
	 */
	public static RegionDAO getInstance() {
		if(instance == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				instance = new RegionDAO();
				instance.cache = null;
			} catch (ClassNotFoundException e) {
				System.err.println("!!! Driver non caricato !!!");
			}
		}
			
		return instance;
	}
	
	/**
	 * Legge tuttie le regioni dal db 
	 * Se la cache è vuota interroga al db, altrimenti ritorna direttamente i customer nella cache
	 * @return una collection con tutte le regioni
	 * @throws DaoExceptions se si verificano problemi di interrogazione o di connessione
	 */
	public Collection<Region> getRegions() throws DaoExceptions{
		if(cache == null) {
			try {
				//Preparo la collection
				Collection<Region> regions = new LinkedList<Region>();
				//Apro la connessione
				openNewConnection();
				
				//Eseguo la query di select
				String query = "SELECT * FROM regions";
				statement = getNewStatement(query);
				statement.execute();
				
				//Popolo la collection con i vari customer
				resultSet = statement.getResultSet();
				
				while(resultSet.next()) {
					int regionID = resultSet.getInt(ColumnNames.regionID.toString());
					String regionName = resultSet.getString(ColumnNames.regionName.toString());
	
					//Costruisco il customer e lo aggiungo alla collection
					Region region = new Region(regionID, regionName);
					regions.add(region);
				}

				cache = regions;
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
	
	public Collection<City> getCities(Region region) throws DaoExceptions{
		try {
			Collection<City> cities = new LinkedList<City>();
			int regionID = region.getRegionID();
			
			openNewConnection();
			
			//Eseguo la query di select con join
			String query = "SELECT (initials, city_name) FROM (regions join cities) "
					+ "where (region.reg_id = cities.region and region.reg_id = ? ";
			statement = getNewStatement(query);
			statement.setInt(1, regionID);
			statement.execute();
			
			//Popolo la collection con i vari customer
			resultSet = statement.getResultSet();
			
			while(resultSet.next()) {
				char[] initials = resultSet.getString("initials").toCharArray();
				String cityName = resultSet.getString("city_name");
		
				//Costruisco il customer e lo aggiungo alla collection
				City c = new City(initials, cityName, regionID);
				cities.add(c);
			}
			
			return cities;
		
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
		regionID("reg_id"),
		regionName("region_name");
		
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
