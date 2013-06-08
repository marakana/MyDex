package com.marakana.mydex.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DriverManagerDataSourceAdapter implements DataSource {

	private final String url;
	private final String username;
	private final String password;

	public DriverManagerDataSourceAdapter(String driver, String url,
			String username, String password) throws ClassNotFoundException {
		Class.forName(driver);
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return DriverManager.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		DriverManager.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		DriverManager.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return DriverManager.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (this.username == null || this.password == null) {
			return DriverManager.getConnection(this.url);
		} else {
			return DriverManager
					.getConnection(this.url, this.username, this.password);
		}
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return DriverManager.getConnection(this.url, username, password);
	}
}
