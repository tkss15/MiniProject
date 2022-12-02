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
import java.util.Scanner;

import gui.ServerInterfaceController;
 
public class DBConnect 
{
	class Constants {
		public static final String DB_SCHEMA = "db ekrut";
		public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+ DB_SCHEMA + "?serverTimezone=IST";
		public static final String DB_USER = "root";
		public static final String DB_PASSWORD = "Huq106jgf68!";
		public static final String DB_Name = "subscriber";
	}
	public static Connection connectToDB()
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
            
        } catch (Exception ex) {
        	 System.out.println("Driver definition failed");
        }
        try 
        {
            Connection conn = DriverManager.getConnection(Constants.DB_URL,Constants.DB_USER,Constants.DB_PASSWORD);
            System.out.println("SQL connection succeed");
 
            return conn;
     	} 
        catch (SQLException ex) 
     	{/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } 
        return null;
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
				queryBuilder.append(Constants.DB_Name);
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
//	public static void printCourses(Connection con)
//	{
//	    Scanner scannerInput = new Scanner(System.in);  // Create a Scanner object
//		Statement stmt;
//		try 
//		{
//			stmt = con.createStatement();
//			System.out.println("Enter Updated arrivel time for Flight KU101:");
//			String date = scannerInput.nextLine();
//			stmt.executeUpdate(("UPDATE Flights SET flight_status = \"" + date + "\" WHERE flight=\"KU101\";"));
//			System.out.println("Updated time");
// 
//			stmt = con.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT flight,flight_status,scheduled FROM flights WHERE arriving_from = \"PARIS\";");
//			while(rs.next())
//	 		{
//				String[] time = (rs.getString(3).split(":"));
//				if(Integer.parseInt(time[0]) >= 15)
//					continue;
//				 String delayed = new String("DELAYED 15:00");
//				 Statement stmt1 = con.createStatement();
//				 stmt1.executeUpdate(("UPDATE Flights SET flight_status = \"" + delayed + "\" WHERE flight=\""+ rs.getString(1) +"\";"));
//				 System.out.println(rs.getString(3) + " " + rs.getString(1)+"  " +rs.getString(2)+ " Changed into ->" + rs.getString(1) + " " + delayed);
//			} 
//			rs.close();
//			System.out.println("Updated Delay time to be 15:00");
// 
//		} catch (SQLException e) {
//			e.printStackTrace();	
//		}
//	}
 
 
	public static void createTableCourses(Connection con1){
		Statement stmt;
		try {
			stmt = con1.createStatement();
			stmt.executeUpdate("create table courses(num int, name VARCHAR(40), semestr VARCHAR(10));");
			stmt.executeUpdate("load data local infile \"courses.txt\" into table courses");
 
		} catch (SQLException e) {	e.printStackTrace();}
 
	}
}
