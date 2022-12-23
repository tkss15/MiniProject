package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import common.ChatIF;
 
public class DBConnect 
{
	ChatIF serverUI;
	Connection conn;
	//jdbc:mysql://127.0.0.1:3306/?user=root
	class Constants 
	{
		public static final String DB_SCHEMA = "ekrutdatabase";
		public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+ DB_SCHEMA + "?serverTimezone=IST";
	}
	private ArrayList<String> databaseDetails;
	public DBConnect(ChatIF serverUI, ArrayList<String> databaseDetails)
	{
		this.databaseDetails = databaseDetails;
		this.serverUI = serverUI;
	}
	public void disconnectFromDB()
	{
		try 
		{
			if(conn != null)
				conn.close();
			serverUI.display("DB disconnection succeeded");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	public void connectToDB()
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            serverUI.display("Driver definition succeeded");
        } catch (Exception ex) {
        	serverUI.display("Driver definition failed");
        }
        try 
        {
            conn = DriverManager.getConnection(Constants.DB_URL,databaseDetails.get(3),databaseDetails.get(4));
            serverUI.display("SQL connection succeeded");
     	} 
        catch (SQLException ex) 
     	{/* handle any errors*/
        	serverUI.display("SQLException: " + ex.getMessage());
        	serverUI.display("SQLState: " + ex.getSQLState());
        	serverUI.display("VendorError: " + ex.getErrorCode());
        } 
   	}
	public void makeQuery(String request, String table, String Where, String Set)
	{
		
		String query = "SELECT subscribernumber FROM subscriber WHERE subscribernumber = ?";
		String operationQuery;
		/*
		 * /test/!name=3&c=2?name1=value1&name2=value2
		 * URL = users/delete/where?customerID=1
		 * Update - put - UPDATE Customers SET ContactName = 'Alfred Schmidt', City= 'Frankfurt' WHERE CustomerID = 1;
		 * Delete - delete - DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
		 * Insert - post - INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country) VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
		 * SELECT - get - SELECT * FROM Customers WHERE Country='Mexico';
		 * */
		
		switch(request)
		{
			case "Put":
				operationQuery = "UPDATE";
				break;
			case "Delete":
				operationQuery = "DELETE";
				break;
			case "Post":
				operationQuery = "Insert";
				break;
			case "Get":
				operationQuery = "SELECT";
				break;
		}
		
	}
	public Boolean updateUserToDB(Connection conn, HashMap<String,String> data)
	{
		try {
			
			PreparedStatement stmt = conn.prepareStatement("SELECT subscribernumber FROM subscriber WHERE subscribernumber = ?");
			stmt.setString(1,data.get("Subscriber Number"));
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return false;
			rs.close();
			PreparedStatement UpdateStatement = conn.prepareStatement("UPDATE subscriber SET creditcardnumber=?,subscribernumber=? WHERE (ID = ?);");
			UpdateStatement.setString(1, data.get("Credit Card Number"));
			UpdateStatement.setString(2, data.get("Subscriber Number"));
			UpdateStatement.setString(3, data.get("ID"));
			UpdateStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();	
		}
		return false;
	}
	public Connection getConn() {
		return conn;
	}
	public HashMap<String,String> searchUserInDB(Connection conn,String id) {
		String finalId = id.trim();
		serverUI.display("Seraching user" +id);
		//Statement stmt;
		HashMap<String,String> subscriber = new LinkedHashMap<>();
		try {
			//stmt = conn.createStatement();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM subscriber WHERE (ID = ?);");
			//ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM subscriber WHERE (ID = ?);",id));
			stmt.setString(1,finalId);
			ResultSet rs = stmt.executeQuery();
			
			if(!rs.next())
			{
				return null;
			}
			// Print out the values
			subscriber.putIfAbsent("First Name", rs.getString(1));
			subscriber.putIfAbsent("Last Name", rs.getString(2));
			subscriber.putIfAbsent("ID", rs.getString(3));
			subscriber.putIfAbsent("Phone Number", rs.getString(4));
			subscriber.putIfAbsent("Email Address", rs.getString(5));
			subscriber.putIfAbsent("Credit Card Number", rs.getString(6));
			subscriber.putIfAbsent("Subscriber Number", rs.getString(7));
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		serverUI.display("Found" +subscriber.toString());
		return subscriber;
	}
}
