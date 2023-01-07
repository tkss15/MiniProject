package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.ChatIF;
import common.RequestObjectClient;
import common.ResponseObject;
 
public class DBConnect 
{
	ChatIF serverUI;
	Connection conn;
	private Map<String,String> SQLCondition = new HashMap<>();
	private Map<String,String> SQLValues = new HashMap<>();

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
            conn = DriverManager.getConnection(Constants.DB_URL,databaseDetails.get(3),databaseDetails.get(4));
            serverUI.display("SQL connection succeeded");
            //AddPhotos();
     	} 
        catch (SQLException ex) 
     	{/* handle any errors*/
        	serverUI.display("SQLException: " + ex.getMessage());
        	serverUI.display("SQLState: " + ex.getSQLState());
        	serverUI.display("VendorError: " + ex.getErrorCode());
        } 
   	}
	/*
	 * Update - PUT - 
	 * 		- URL: table=subscriber#condition=id=4#values=creditcardnumber=3&subscribernumber=4
	 * 		- UPDATE subscriber SET subscribernumber = 4, creditcardnumber = 3 WHERE id = 4;
	 * Delete - DELETE - 
	 * 		- table=subscriber#values=id=3
	 * 		- DELETE FROM subscriber WHERE id=3
	 * Insert - POST - 
	 * 		- table=subscriber#values=id=3&username=tkss15&lastname=shneor
	 * 		- INSERT INTO subscriber (id,username,lastname) VALUES ('3','tkss15','shneor')
	 * SELECT - GET
	 * 		- table=users#condition=userName=%s#values=userName=username&userPassword=password
	 * 		- SELECT userPassword, userName FROM users WHERE userName = "tkss15"
	 * Gal Changed
	 * WildCard -
	 * 
	 * */
	public String CreateSqlStatement(RequestObjectClient reqObject)
	{
		StringBuilder query = new StringBuilder();
		String Table = reqObject.getTable();
		switch(reqObject.getSQLOpreation())
		{
			case "PUT":
			{
				query.append("UPDATE " + Table);
				if(!SQLValues.isEmpty())
				{					
					query.append(" SET ");
				    for (Map.Entry<String,String> entry : SQLValues.entrySet()) 
				    {
				    	query.append(entry.getKey() + " = " + entry.getValue());
				    	query.append(", ");				    	
				    }
				    query.delete(query.length()-2, query.length());
				}
				
				if(!SQLCondition.isEmpty())
				{
					query.append(" WHERE (" );
					for (Map.Entry<String,String> entry : SQLCondition.entrySet()) 
					{
						query.append(entry.getKey() + " = " + String.format("\"%s\"", entry.getValue()));
						query.append("AND ");
					}
					query.delete(query.length()-4, query.length());
					query.append(")");
				}
				break;
			}
			case "DELETE": 
			{
				query.append("DELETE FROM " + Table);
				if(!SQLCondition.isEmpty())
				{
					query.append(" WHERE (" );
					for (Map.Entry<String,String> entry : SQLCondition.entrySet()) 
					{
						query.append(entry.getKey() + " = " + String.format("\"%s\"", entry.getValue()));
						query.append("AND ");
					}
					query.delete(query.length()-4, query.length());
					query.append(")");
				}
				break;
			}
			case "POST": 
			{
				query.append("INSERT INTO ");
				query.append(Table + " (");
				if(!SQLValues.isEmpty())
				{					
					for (Map.Entry<String,String> entry : SQLValues.entrySet()) 
						query.append(entry.getKey() + ",");
					query.deleteCharAt(query.length()-1);
					query.append(") VALUES (");
					for (Map.Entry<String,String> entry : SQLValues.entrySet()) 
						query.append(String.format("\'%s\'", entry.getValue())  + ",");
					query.deleteCharAt(query.length()-1);
					query.append(")");
				}
				break;
			}
			case "GET":
			{
				query.append("SELECT ");
				
				if(SQLValues.isEmpty())
				{					
					query.append("*");
				}
				else
				{
			       for (Map.Entry<String,String> entry : SQLValues.entrySet()) 
			    	   query.append(entry.getKey() + ", ");
			       query.delete(query.length()-2, query.length());
				}
				query.append(" FROM " + Table);

				if(!SQLCondition.isEmpty())
				{
					query.append(" WHERE (" );
					for (Map.Entry<String,String> entry : SQLCondition.entrySet()) 
					{
						query.append(entry.getKey() + " = " + String.format("\"%s\"", entry.getValue()));
						query.append("AND ");
					}
					query.delete(query.length()-4, query.length());
					query.append(")");
				}
				break;	
			}
			
		}
		serverUI.display(query.toString());
		return query.toString();	
	}

	public void CreateOpreation(RequestObjectClient reqObject)
	{
		if(reqObject.getSQLOpreation().equals("*"))
			return;
		
		String[] arrRequest = reqObject.getURL().split("#");
		reqObject.setTable((arrRequest[0].split("="))[1]);
		
		SQLCondition.clear();
		SQLValues.clear();
		
		for(String loop : arrRequest)
		{
			if(loop.startsWith("condition="))
			{
				String req = (loop.split("condition="))[1];
				String[] Conditions = req.split("&");
				for(String pair : Conditions)
				{
					 String[] keyValue = pair.split("=");
					 String key = keyValue[0];
					 String value = keyValue[1];
					 SQLCondition.put(key, value);
				}
			}
			if(loop.startsWith("values="))
			{
				String req = (loop.split("values="))[1];
				String[] Values = req.split("&");
				for(String pair : Values)
				{
					 String[] keyValue = pair.split("=");
					 String key = keyValue[0];
					 String value = keyValue[1];
					 SQLValues.put(key, value);
				}
			}
		}
	}
		/*
		 * /table=tablename?condition=condition?Values=set
		 * */
	public Connection getConn() 
	{
		return conn;
	}


	public ResponseObject makeQuery(RequestObjectClient query)
	{
		String queryTable = "WildCard";
		if(query.getSQLOpreation().equals("*"))
		{
			query.setTable(queryTable);
		}
		serverUI.display(query.getURL());
		CreateOpreation(query);
		
		ResponseObject res = new ResponseObject(query.getTable());
		res.setRequest(query.getRequestID());
		
		try 
		{
			PreparedStatement stmt;
			ResultSet rs;
			
			if(query.getSQLOpreation().equals("*"))
			{
				stmt = (conn.prepareStatement(query.getURL()));
				if(!query.getURL().startsWith("SELECT"))
				{
					int result;
					result = stmt.executeUpdate();
					res.addObject(result);
					return res;
				}
			}
			else
				stmt = (conn.prepareStatement(CreateSqlStatement(query)));
			
			
			if(query.getSQLOpreation().equals("PUT") || query.getSQLOpreation().equals("DELETE") || query.getSQLOpreation().equals("POST"))
			{
				int result;
				result = stmt.executeUpdate(CreateSqlStatement(query));
				res.addObject(result);
				return res;
			}
			rs = stmt.executeQuery();
			
			ResultSetMetaData rsdm = rs.getMetaData();
			int columnCount = rsdm.getColumnCount();
			
			while(rs.next())
			{
				Object[] values = new Object[columnCount];
				for(int j = 1; j <= columnCount; j++)
				{		
					if(rsdm.getColumnName(j).equals("ProductPicture"))
					{
						Blob blob = rs.getBlob(j);
						if(blob == null)
							continue;
						
						byte byteArray[] = blob.getBytes(1, (int)blob.length());
						res.ResponsePicture.add(byteArray);
					}
					else
						values[j-1] = rs.getObject(j);
				}
				res.addObject(values);
			}
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
