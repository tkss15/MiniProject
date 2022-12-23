package database;

import java.util.ArrayList;

public class RequestObject {
	/*
	 * /test/!name=3&c=2?name1=value1&name2=value2
	 * URL = users/delete/Set?....Where?.....
	 * Update - put - UPDATE Customers SET ContactName = 'Alfred Schmidt', City= 'Frankfurt' WHERE CustomerID = 1;
	 * Delete - delete - DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';
	 * Insert - post - INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country) VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
	 * SELECT - get - SELECT * FROM Customers WHERE Country='Mexico';
	 * */
	private String SQLOpreation;
	private String URL;
	private ArrayList<String> Set = new ArrayList<>();
	private String Table;
	public RequestObject(String URL)
	{
		this.URL = URL;
		CreateOpreation();
	}
	/*
	 * Put [ Set?name1=val&name2=val&...Where?name=val1
	 * 
	 * 
	 * */
	private void CreateOpreation()
	{
		int findTable = URL.indexOf("/");
		int findOpreation = URL.indexOf(findTable, '/');
		int findSet = URL.indexOf("Set?");
		int findWhere = (URL.indexOf("Where") == -1) ? URL.length() - 1 : URL.indexOf("Where");
		
		Table = URL.substring(0, findTable-1);
		SQLOpreation = URL.substring(findTable, findOpreation-1);
		
		for(int i = findSet; i < findWhere; i++)
		{
			
		}
		
	}
	
	
}
