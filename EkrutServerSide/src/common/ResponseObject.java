package common;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseObject implements Serializable
{
	
	/**
	 * 
	 */
	private String RequestID;
	public String Table;
	public ArrayList<Object> Responsedata;
	private static final long serialVersionUID = -37052886646462340L;

	public ResponseObject(String Table)
	{
		this.Table = Table;
		Responsedata = new ArrayList<>();
	}
	
	public void addObject(Object data)
	{
		Responsedata.add(data);
	}
	public void setRequest(String RequestID)
	{
		this.RequestID = RequestID;
	}
}
