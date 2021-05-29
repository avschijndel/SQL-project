package application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Server {
	private static StringProperty databaseName = new SimpleStringProperty();
	private static StringProperty serverName = new SimpleStringProperty();
	private static String url;
	private static Connection con;
	private static Boolean connectedToServer = false;
	private static Boolean connectedToDatabase = false;
	private static ArrayList<String> databaseList = new ArrayList<>();
	
	
	public static Boolean tryConnectToServer(String serverUrl) {
		try {
			DriverManager.setLoginTimeout(1);
			con = DriverManager.getConnection("jdbc:sqlserver://" 
					+ serverUrl + ":1433;"
	                + "integratedSecurity=true;");
			serverName.setValue(serverUrl);
			connectedToServer = true;
			System.out.println("Connected to " + serverName.getValue());
		} catch (SQLException ex) {
			connectedToServer = false;
			SimpleErrorMessage.display("No connection",  "Cannot connect to server '" + serverUrl + "'");
		}
		return connectedToServer;
	}
	
	public static Boolean tryConnectToDatabase(String dbName) {
		try {
			DriverManager.setLoginTimeout(1);
			con = DriverManager.getConnection("jdbc:sqlserver://" + serverName.getValue() + ":1433;"
	                + "database=" + dbName
	                + ";integratedSecurity=true;");
			databaseName.setValue(dbName);
			connectedToDatabase = true;
			System.out.println("Connected to " + databaseName.getValue());
		} catch (SQLException ex) {
			connectedToDatabase = false;
			SimpleErrorMessage.display("No connection",  "Cannot connect to database '" + databaseName.getValue() + "'");
		}
		return connectedToDatabase;
	}

	public static ArrayList<String> getDatabaseList(){
		try {
			databaseList.clear();
//			con = DriverManager.getConnection("jdbc:sqlserver://" + serverName.getValue() + ":1433;integratedSecurity=true;");
			DatabaseMetaData meta = con.getMetaData();
		    ResultSet res = meta.getCatalogs();
		    while (res.next()) {
		       databaseList.add(res.getString("TABLE_CAT"));
		    }
		} catch (Exception ex) {
			SimpleErrorMessage.display("Error", "Cannot find any database");
		}
		
		return databaseList;
	}
	
	// Getters and setters
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String newUrl) {
		url = newUrl;
	}
	public static Connection getCon() {
		return con;
	}
	public static void setCon(Connection con) {
		Server.con = con;
	}
	public static StringProperty getDatabaseName() {
		return databaseName;
	}
	public static void setDatabaseName(String newDatabaseName) {
		databaseName.setValue(newDatabaseName);
	}
	public static StringProperty getServerName() {
		return serverName;
	}
	public static void setServerName(String newServerName) {
		serverName.setValue(newServerName);
	}


}
