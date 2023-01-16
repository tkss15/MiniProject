package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ResponseObject is a class that implements the Serializable interface.
 * The class holds the data for a server response to a client request, 
 * including the RequestID, Table name, and the data and pictures of the response.
 * The class provides methods for initializing and adding data and pictures to the response, 
 * as well as getters and setters for the RequestID field.
 */
public class ResponseObject implements Serializable
{
	
	private String RequestID;
	public String Table;
	
	public ArrayList<Object> Responsedata;// Will hold all the received data from the Server without pictures
	public ArrayList<byte[]> ResponsePicture;// Will hold all the pictures from the Server.
	
	private int size=0;
	public  byte[] mybytearray;
	
	private static final long serialVersionUID = -37052886646462340L;

	public ResponseObject(String Table)
	{
		this.Table = Table;
		Responsedata = new ArrayList<>();
		ResponsePicture = new ArrayList<>();
	}
	
	public String getRequest() {
		return RequestID;
	}

	public void setRequestID(String requestID) {
		RequestID = requestID;
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
