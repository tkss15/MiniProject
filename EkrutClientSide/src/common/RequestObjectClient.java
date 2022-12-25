package common;

import java.io.Serializable;

public class RequestObjectClient implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8888712460171709570L;
	private String SQLOpreation;
	private String URL;
	
	public RequestObjectClient(String URL, String SQLOpreation)
	{
		this.URL = URL;
		this.SQLOpreation = SQLOpreation;
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
