package com.webapp.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.webapp.bean.City;
import com.webapp.bean.Region;
import com.webapp.dao.DaoException;
import com.webapp.dao.IRegionDAO;

public class RegionDaoJDBC implements IRegionDAO{
	private static IRegionDAO instance;
	private Collection<Region> cache;
	
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement statement;
	
	private DBPropertiesManager dbPropertiesManager;
	
	private RegionDaoJDBC() {	
		try {
			cache = null;
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
	public static IRegionDAO getInstance() {
		if(instance == null)
			instance = new RegionDaoJDBC();
		return instance;
	}
	
	/**
	 * Legge tutte le regioni dal db 
	 * Se la cache è vuota interroga il db, altrimenti ritorna direttamente
	 * i customer nella cache
	 * @return una collection con tutte le regioni
	 * @throws DaoException se si verificano problemi di interrogazione o di connessione
	 */
	@Override
	public Collection<Region> getRegions() throws DaoException{
		if(cache == null) {
			try {
				Collection<Region> regions = new LinkedList<Region>();
				openNewConnection();
				
				String query = "SELECT * FROM region";
				statement = getNewStatement(query);
				statement.execute();
				
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
	
	@Override
	public Collection<City> getCities(int regionID) throws DaoException{
		try {
			Collection<City> cities = new LinkedList<City>();
			
			openNewConnection();
			
			//Eseguo la query di select con join
			String query = "SELECT initials, city_name FROM (region JOIN city) "
					+ "WHERE (region.reg_id=city.region and region.reg_id=?)";
			
			statement = getNewStatement(query);
			statement.setInt(1, regionID);
			
			statement.execute();
			
			resultSet = statement.getResultSet();
			
			while(resultSet.next()) {
				char[] initials = resultSet.getString("initials").toCharArray();
				String cityName = resultSet.getString("city_name");		
				City c = new City(initials, cityName, regionID);
				cities.add(c);
			}
			
			return cities;
		
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
