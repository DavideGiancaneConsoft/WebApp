package com.webapp.dao.ojdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import com.webapp.bean.City;
import com.webapp.bean.Region;
import com.webapp.dao.DBPropertiesManager;
import com.webapp.dao.DaoException;
import com.webapp.dao.IRegionDAO;
import com.webapp.dao.DBPropertiesManager.DBType;

import oracle.jdbc.OracleTypes;

public class RegionDaoOJDBC implements IRegionDAO{
	private static IRegionDAO instance;
	private DBPropertiesManager dbPropertiesManager;
	private Connection connection;
	
	private RegionDaoOJDBC() {
		try {
			dbPropertiesManager = DBPropertiesManager.getInstance();
			Class.forName(dbPropertiesManager.getDatabaseDriver(DBType.OracleDB));
		} catch (Exception e) {
			System.err.println("!!! Error: " + e.getMessage() + " !!!");
		}
	}
	
	public static IRegionDAO getInstance() {
		if(instance == null)
			instance = new RegionDaoOJDBC();
		return instance;
	}
	
	@Override
	public Collection<Region> getRegions() throws DaoException {
		Collection<Region> regions = null;
		ResultSet rs = null;
		try {
			//Apro la connessione, preparo la chiamata alla procedura ed imposto il tipo di ritorno
			openNewConnection();
			CallableStatement stmt = connection.prepareCall("{call residency_package.get_all_regions(?)}");
			stmt.registerOutParameter(1, OracleTypes.CURSOR);
			
			//Eseguo la procedura
			stmt.execute();
			
			//Prendo l'oggetto di outout e lo casto in result set per poterlo analizzare
			rs = (ResultSet) stmt.getObject(1);
			
			//Vado a riempire la collection
			regions = new LinkedList<>();
			while(rs.next()) {
				int regionID = rs.getInt("reg_id");
				String regionName = rs.getString("region_name");
				regions.add(new Region(regionID, regionName));
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage());
		} finally {
			try {
				rs.close();
				connection.close();
			} catch (SQLException ex) {
				System.err.println("Error: " + ex.getMessage());
			} catch (NullPointerException ignored) {}
		}
		return regions;
	}

	@Override
	public Collection<City> getCities(int regionID) throws DaoException {
		Collection<City> cities = null;
		ResultSet rs = null;
		
		try {
			//Apro la connessione, preparo la chiamata alla procedura ed imposto i parametri IN e OUT
			openNewConnection();
			CallableStatement stmt = connection.prepareCall("{call residency_package.get_cities_by_region(?,?)}");
			stmt.setInt(1, regionID);
			stmt.registerOutParameter(2, OracleTypes.CURSOR);
			
			//Eseguo la procedura
			stmt.execute();
			
			//Prendo l'oggetto di outout e lo casto in result set per poterlo analizzare
			rs = (ResultSet) stmt.getObject(2);
			
			//Vado a riempire la collection
			cities = new LinkedList<>();
			while(rs.next()) {
				char[] initials = rs.getString("initials").toCharArray();
				String cityName = rs.getString("city_name");
				int region = rs.getInt("region");
				cities.add(new City(initials, cityName, region));
			}
		} catch (SQLException e) {
			try {
				rs.close();
				connection.close();
			} catch (SQLException ex) {
				System.err.println("Error: " + ex.getMessage());
			} catch (NullPointerException ignored) {}
		}
		return cities;
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
