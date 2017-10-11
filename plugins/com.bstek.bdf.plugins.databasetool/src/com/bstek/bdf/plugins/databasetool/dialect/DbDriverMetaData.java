package com.bstek.bdf.plugins.databasetool.dialect;

import java.io.Serializable;

public class DbDriverMetaData implements Serializable {
	private static final long serialVersionUID = 1L;

	private String dbType;
	private String driverClassName;
	private String driverLocation;
	private String serverName;
	private int serverPort;
	private String dataBaseName;
	private String username;
	private String password;
	private String url;
	private String dbSchema;
	private String dbCatalog;


	public String getDriverClassName() {
		return driverClassName;
	}

	public String getDriverLocation() {
		return driverLocation;
	}

	public void setDbNmae(String dbNmae) {
		this.dbType = dbNmae;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public void setDriverLocation(String driverLocation) {
		this.driverLocation = driverLocation;
	}

	public String getDbType() {
		return dbType;
	}

	public String getServerName() {
		return serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getDataBaseName() {
		return dataBaseName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getDbCatalog() {
		return dbCatalog;
	}

	public void setDbCatalog(String dbCatalog) {
		this.dbCatalog = dbCatalog;
	}

}
