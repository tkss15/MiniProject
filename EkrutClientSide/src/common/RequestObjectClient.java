package common;

import java.io.Serializable;

public class RequestObjectClient implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8888712460171709570L;
	private String SQLOpreation;
	private String URL;
	private String RequestID;
	
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
