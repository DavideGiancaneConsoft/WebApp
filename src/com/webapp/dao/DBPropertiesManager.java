package com.webapp.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBPropertiesManager {
	
	private static DBPropertiesManager instance;
	private Properties prop;
	
	private DBPropertiesManager() {
		prop = new Properties();
		String propFileName = "db.properties";
		InputStream inputStream = DBPropertiesManager.class
				.getClassLoader().getResourceAsStream(propFileName);
		try {
			prop.load(inputStream);
		} catch (IOException e) {
			System.err.println("*** DB property file not found ***");
		}
	}
	
	public static DBPropertiesManager getInstance() {
		if(instance == null)
			instance = new DBPropertiesManager();
		return instance;
	}
	
	public String getJdbcDriver() {
		return prop.getProperty("JDBC_DRIVER");
	}
	
	public String getDbURI() {
		return prop.getProperty("DB_URI");
	}
	
	public String getUser() {
		return prop.getProperty("DB_USER");
	}
	
	public String getPsw() {
		return prop.getProperty("DB_PSW");
	}
}
