package Database;

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

import gui.ServerInterfaceController;
 
public class DBConnect 
{
	ServerInterfaceController serverUIController;
	Connection conn;
	public Connection getConn() {
		return conn;
	}
	//jdbc:mysql://127.0.0.1:3306/?user=root
	class Constants {
		public static final String DB_SCHEMA = "ekrutdatabase";
		public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+ DB_SCHEMA + "?serverTimezone=IST";
		public static final String DB_USER = "root";
		public static final String DB_PASSWORD = "Shaya99Dd";
		public static final String DB_Name = "subscriber";
	}
	
	public DBConnect(ServerInterfaceController serverUIController)
	{
		this.serverUIController = serverUIController;
	}
	public void disconnectFromDB()
	{
		try 
		{
			if(conn != null)
				conn.close();
			serverUIController.writeToConsole("DB disconnection succeeded");
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
            serverUIController.writeToConsole("Driver definition succeeded");
        } catch (Exception ex) {
            serverUIController.writeToConsole("Driver definition failed");
        }
        try 
        {
            conn = DriverManager.getConnection(Constants.DB_URL,Constants.DB_USER,Constants.DB_PASSWORD);
            serverUIController.writeToConsole("SQL connection succeeded");
     	} 
        catch (SQLException ex) 
     	{/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
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
			
			while (rs.next()) {
				// Print out the values
				subscriber.putIfAbsent("First Name", rs.getString(1));
				subscriber.putIfAbsent("Last Name", rs.getString(2));
				subscriber.putIfAbsent("ID", rs.getString(3));
				subscriber.putIfAbsent("Phone Number", rs.getString(4));
				subscriber.putIfAbsent("Email Address", rs.getString(5));
				subscriber.putIfAbsent("Credit Card Number", rs.getString(6));
				subscriber.putIfAbsent("Subscriber Number", rs.getString(7));
				
				System.out.println(
						rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4)+"  "
						+ rs.getString(5) + "  " + rs.getString(6) + "  " + rs.getString(7));
			}
			rs.close();
			// stmt.executeUpdate("UPDATE course SET semestr=\"W08\" WHERE num=61309");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriber;
	}
}
