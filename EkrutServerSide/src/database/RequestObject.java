package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestObject {
	/*
	 * /table=tablename&condition=condition&Set=set
	 * 
	 * Update - put - UPDATE Customers SET ContactName = 'Alfred Schmidt', City= 'Frankfurt' WHERE CustomerID = 1;
	 * Delete - delete - DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
	 * Insert - post - INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country) VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
	 * SELECT - get - SELECT * FROM Customers WHERE Country='Mexico';
	 * */
	private String SQLOpreation;
	private String URL;
	private Map<String,String> SQLCondition = new HashMap<>();
	private Map<String,String> SQLValues = new HashMap<>();
	private String Table;
	public RequestObject(String URL, String SQLOpreation)
	{
		this.URL = URL;
		this.SQLOpreation = SQLOpreation;
	}
	public String getTable()
	{
		return Table;
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
	 * 		- 
	 * 		- SELECT * FROM Customers WHERE Country='Mexico';
	 * Gal Changed
	 * WildCard - DELIVERY
	 * 	-
	 * 
	 * */
	public String CreateSqlStatement()
	{
		StringBuilder query = new StringBuilder();
		switch(SQLOpreation)
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
					query.append(" WHERE " );
					for (Map.Entry<String,String> entry : SQLCondition.entrySet()) 
					{
						query.append(entry.getKey() + " = " + entry.getValue());
						query.append(", ");
					}
					query.delete(query.length()-2, query.length());
				}
				break;
			}
			case "DELETE": 
			{
				query.append("DELETE FROM " + Table);
				if(!SQLValues.isEmpty())
				{					
					query.append(" WHERE ");
					for (Map.Entry<String,String> entry : SQLValues.entrySet()) 
						query.append(entry.getKey() + "=" + entry.getValue());
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
					query.append(" WHERE " );
					for (Map.Entry<String,String> entry : SQLCondition.entrySet()) 
					{
						query.append(entry.getKey() + " = " + String.format("\"%s\"", entry.getValue()));
						query.append("AND ");
					}
					query.delete(query.length()-4, query.length());
				}
				break;	
			}
		}
		return query.toString();
	}
	public void CreateOpreation()
	{
		/*
		 * /table=tablename?condition=condition?Values=set
		 * */
		String[] arrRequest = URL.split("#");
		Table = (arrRequest[0].split("="))[1];
		
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
	
	
}
