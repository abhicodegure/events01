package com.rest.test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public enum EventsDBDataSource implements DataSource{
	datasource;
	private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static String USER_NAME = "root";
    private static String PASSWORD = "abhiroot";

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public Connection getConnection() throws SQLException {
		Connection con = null;
		try {
			Class.forName(DRIVER_CLASS_NAME);
			con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/test_create_events",USER_NAME,PASSWORD); 
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

	public Connection getConnection(String username, String password) throws SQLException {
		
		Connection con = null;
		try {
			Class.forName("DRIVER_CLASS_NAME");
			con=DriverManager.getConnection(  
					"jdbc:mysql://localhost:3306/test_create_events",USER_NAME,PASSWORD); 
		} catch (Exception e) {
			
		}
		return con;
	}

	
	
}
