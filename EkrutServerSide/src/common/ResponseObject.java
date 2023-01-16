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
	public ArrayList<byte[]> ResponsePicture;
	private int size=0;
	public  byte[] mybytearray;
	private static final long serialVersionUID = -37052886646462340L;

	public ResponseObject(String Table)
	{
		this.Table = Table;
		Responsedata = new ArrayList<>();
		ResponsePicture = new ArrayList<>();
	}
	
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
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
