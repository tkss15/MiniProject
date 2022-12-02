package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gui.ServerInterfaceController;
 
public class DBConnect 
{
	ServerInterfaceController serverUIController;
	Connection conn;
	//jdbc:mysql://127.0.0.1:3306/?user=root
	private String DB_USER,DB_PASSWORD,DB_NAME;
	class Constants {
		public static final String DB_SCHEMA = "ekrutdatabase";
		public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+ DB_SCHEMA + "?serverTimezone=IST";
		//public static final String DB_USER = "root";
		//public static final String DB_PASSWORD = "nset1234!@#$";
		//public static final String DB_Name = "subscriber";
	}
	
	public DBConnect(ServerInterfaceController serverUIController,String DB_USER, String DB_PASSWORD, String DB_NAME)
	{
		this.DB_USER = DB_USER;
		this.DB_PASSWORD = DB_PASSWORD;
		this.DB_NAME = DB_NAME;
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
            conn = DriverManager.getConnection(Constants.DB_URL,DB_USER,DB_PASSWORD);
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
	public static void saveUserToDB(Connection con, ArrayList<String> data)
	{	
		Map<String,String> dataparsing = parsingTheData(data);
		System.out.println(dataparsing);
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT ID FROM userdata WHERE ID = ?");
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
 
				PreparedStatement insertstat = con.prepareStatement(queryBuilder.toString());
 
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
