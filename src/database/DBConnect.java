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
	public Connection getConn() {
		return conn;
	}
	//jdbc:mysql://127.0.0.1:3306/?user=root
	class Constants {
		public static final String DB_SCHEMA = "ekrutdatabase";
		public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+ DB_SCHEMA + "?serverTimezone=IST";
		public static final String DB_USER = "root";
		public static final String DB_PASSWORD = "Aa123456";
		public static final String DB_Name = "subscriber";
	}
	
	public DBConnect(ChatIF serverUI)
	{
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
            conn = DriverManager.getConnection(Constants.DB_URL,Constants.DB_USER,Constants.DB_PASSWORD);
            serverUI.display("SQL connection succeeded");
     	} 
        catch (SQLException ex) 
     	{/* handle any errors*/
        	serverUI.display("SQLException: " + ex.getMessage());
        	serverUI.display("SQLState: " + ex.getSQLState());
        	serverUI.display("VendorError: " + ex.getErrorCode());
        } 
   	}
	public static Map<String,String> parsingTheData(ArrayList<String> data)
	{
		Map<String,String> dataparsing = new HashMap<>();
		dataparsing.put("UserName", data.get(0));
		dataparsing.put("ID", data.get(1));
		dataparsing.put("Department", data.get(2));
		dataparsing.put("Telphone", data.get(3));
		return dataparsing;
 
	}
	public void updateUserToDB(Connection conn, HashMap<String,String> data)
	{
		try {
			PreparedStatement UpdateStatement = conn.prepareStatement("UPDATE subscriber SET creditcardnumber=?,subscribernumber=? WHERE ID = ?;");
			UpdateStatement.setString(1, data.get("Credit Card Number"));
			UpdateStatement.setString(2, data.get("Subscriber Number"));
			UpdateStatement.setString(3, data.get("ID"));
			UpdateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();	
		}
	}
	public static void saveUserToDB(Connection conn, ArrayList<String> data)
	{	
		Map<String,String> dataparsing = parsingTheData(data);
		System.out.println(dataparsing);
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT ID FROM userdata WHERE ID = ?");
			stmt.setString(1,dataparsing.get("ID"));
			ResultSet rs = stmt.executeQuery();
			if(rs.next() == false)
			{
				int i = 1;
				StringBuilder queryBuilder = new StringBuilder();
				String query = new String();
 
				queryBuilder.append("INSERT INTO ");
				//queryBuilder.append(DB_NAME);
				queryBuilder.append("(");
				for (String name : dataparsing.keySet()) 
					queryBuilder.append(name + ",");
				queryBuilder.deleteCharAt(queryBuilder.length() - 1);// Deleting the last ',' which is not needed.
				queryBuilder.append(")");
 
				queryBuilder.append("VALUES (");
				for (String name : dataparsing.values()) 
				{
					queryBuilder.append("?,");
				}
				queryBuilder.deleteCharAt(queryBuilder.length() - 1);// Deleting the last ',' which is not needed.
				queryBuilder.append(")");
 
				PreparedStatement insertstat = conn.prepareStatement(queryBuilder.toString());
 
				for (String name : dataparsing.values()) 
				{
					insertstat.setString(i, name);
					i++;
				}

				insertstat.executeUpdate();
			}
			rs.close();
 
		} catch (SQLException e) {
			e.printStackTrace();	
		}
	}
	public HashMap<String,String> searchUserInDB(Connection conn,String id) {
		Statement stmt;
		HashMap<String,String> subscriber = new LinkedHashMap<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM subscriber WHERE id='%s';",id));
			
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
			
//			System.out.println(
//						rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4)+"  "
//						+ rs.getString(5) + "  " + rs.getString(6) + "  " + rs.getString(7));
			rs.close();
			// stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriber;
	}
}
