package common;

import java.io.Serializable;

import client.ClientUI;

/**
 * RequestObjectClient is a class that implements the Serializable interface.
 * The class holds the data for a client request to a server, including the RequestID, URL, and SQL operation.
 * The class also provides getters and setters for each of these fields.
 */
public class RequestObjectClient implements Serializable 
{
	private static final long serialVersionUID = -8888712460171709570L;
	private String SQLOpreation;
	private String URL;
	private String RequestID;
	
	/**
	 * Constructor for the RequestObjectClient class, which sets the RequestID, URL, and SQL operation.
	 *
	 * @param RequestID the unique ID of the request
	 * @param URL is the params of the SQL query. each param is seperated by a '#'
	 * @param SQLOpreation the SQL operation to be performed
	 * 
	 * 
	 * Exmaple !
	 * 
	 * a new Request will have this params:
	 * "#USER_UPDATE_STATUS", String.format("%s#", ClientUI.clientController.getUser().getUserName()) ,"PUT" 
	 * 
	 * After sending this request to the server the server will look for the correct @RequestID and then will add the given data @URL
	 * to the Sql, we also say what operation we want to do. since we need to update status is a PUT function.
	 * 
	 * On SQLOpreations there is a better explanation on ServSide/database/dbconnect
	 */
	public RequestObjectClient(String RequestID, String URL, String SQLOpreation)
	{
		this.RequestID = RequestID;
		this.URL = URL;
		this.SQLOpreation = SQLOpreation;
	}
	
	public String getRequestID() {
		return RequestID;
	}

	public void setRequestID(String requestID) {
		RequestID = requestID;
	}

	public String getSQLOpreation() {
		return SQLOpreation;
	}

	public void setSQLOpreation(String sQLOpreation) {
		SQLOpreation = sQLOpreation;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
	
	
}
