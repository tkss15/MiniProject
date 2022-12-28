package common;

import java.io.Serializable;

public class RequestObjectClient implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8888712460171709570L;
	private String SQLOpreation;
	private String URL;
	private String Table;
	private String RequestID;
	
	public RequestObjectClient(String URL, String SQLOpreation, String RequestID)
	{
		this.URL = URL;
		this.SQLOpreation = SQLOpreation;
		this.RequestID = RequestID;
	}
	
	public String getRequestID() {
		return RequestID;
	}

	public void setRequestID(String requestID) {
		RequestID = requestID;
	}

	public String getTable() {
		return Table;
	}

	public void setTable(String table) {
		Table = table;
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
