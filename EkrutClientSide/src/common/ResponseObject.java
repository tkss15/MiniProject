package common;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseObject implements Serializable
{
	
	/**
	 * 
	 */
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
	public String getTable()
	{
		return Table;
	}
}
