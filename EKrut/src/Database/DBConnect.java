package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
		public static final String DB_SCHEMA = "db ekrut";
		public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+ DB_SCHEMA + "?serverTimezone=IST";
		public static final String DB_USER = "root";
		public static final String DB_PASSWORD = "Huq106jgf68!";
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
			serverUIController.writeToConsole("DB disconnection succeed");
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
            serverUIController.writeToConsole("Driver definition succeed");
        } catch (Exception ex) {
            serverUIController.writeToConsole("Driver definition failed");
        }
        try 
        {
            conn = DriverManager.getConnection(Constants.DB_URL,Constants.DB_USER,Constants.DB_PASSWORD);
            serverUIController.writeToConsole("SQL connection succeed");
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
				//PreparedStatement insertstat = con.prepareStatement();
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
//				insertstat.setString(1, dataparsing.get("UserName"));
//				insertstat.setString(2, dataparsing.get("ID"));
//				insertstat.setString(3, dataparsing.get("Department"));
//				insertstat.setString(4, dataparsing.get("Telphone"));
 
				insertstat.executeUpdate();
			}
			rs.close();
 
		} catch (SQLException e) {
			e.printStackTrace();	
		}
		//INSERT INTO table_name (column1, column2, column3, ...)
		//VALUES (value1, value2, value3, ...); 
	}
	public HashMap<String,String> searchUserInDB(Connection conn,String id) {
		Statement stmt;
		HashMap<String,String> subscriber = new HashMap<>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM subscriber WHERE id='%s';",id));
			
			while (rs.next()) {
				// Print out the values
				subscriber.putIfAbsent("firstName", rs.getString(1));
				subscriber.putIfAbsent("lastName", rs.getString(2));
				subscriber.putIfAbsent("id", rs.getString(3));
				subscriber.putIfAbsent("phoneNumber", rs.getString(4));
				subscriber.putIfAbsent("emailAddress", rs.getString(5));
				subscriber.putIfAbsent("creditCardNumber", rs.getString(6));
				subscriber.putIfAbsent("subscriberNumber", rs.getString(7));
				
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

	public static void updateArrivalTime(Connection con) {
		Statement stmt;

		try {
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE flights SET status= 'Expected 15:00' WHERE flight='KU101'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

 
//	public static void createTableCourses(Connection con1){
//		Statement stmt;
//		try {
//			stmt = con1.createStatement();
//			stmt.executeUpdate("create table courses(num int, name VARCHAR(40), semestr VARCHAR(10));");
//			stmt.executeUpdate("load data local infile \"courses.txt\" into table courses");
// 
//		} catch (SQLException e) {	e.printStackTrace();}
// 
//	}
}
