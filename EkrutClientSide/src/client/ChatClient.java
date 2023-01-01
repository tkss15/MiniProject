package client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import Entity.Facility;
import Entity.User;
import common.ChatIF;
import common.MyFile;
import common.RequestObjectClient;
import common.ResponseObject;
import common.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient 
{
	public static boolean awaitResponse = false;
	ChatIF clientConsole;
	/***
	 * s
	 * @param host - saves the data of the ip-address the client entered in order to connect to the server.
	 * @param port - saves the data of the port the client entered in order to connect to the server.
	 * @param clientUI - interface that allows the upper layer of client to communicate with lower layers.
	 * @throws IOException
	 * 
	 *ChatClient is a class that communicate directly with the server. is the highest layered class and closest to the sever in terms of layering.
	 */
	public ChatClient(String host, int port, ChatIF clientConsole) throws IOException 
	{
		super(host, port); // Call the superclass constructor
		this.clientConsole = clientConsole;
		openConnection();
		sendToServer(getInetAddress());
	}
	
	/***
	 * @param msg
	 * Function handling @msg that client receives from the server.
	 * every message coming from the server is an object.
	 */
	@Override
	protected void handleMessageFromServer(Object msg) 
	{
		System.out.println("Message arrived");
		if(msg instanceof ResponseObject)
		{
			System.out.println(((ResponseObject)msg).getRequest() + "Handled");
			ResponseObject serverResponse = (ResponseObject) msg;
			
			// ClientUI dosent have IController and CANT implement it. so sadly it have to happen over here.
			if(serverResponse.getRequest().equals("#FIRST_INSTALL"))
			{	
					System.out.println("First Install");
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
							//	public Facility(int FacilityID, String FacilityLocation, String FacilityName, int FacilityThresholder)
							
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						String FacilityArea = (String)values[1];
						String FacilityLocation = (String)values[2];
						String FacilityName = (String)values[3];
						Integer FacilityThresholder = (Integer)values[4];
						boolean FacilityEK = (boolean) values[5];
							//ClientUI.clientController.(new Facility(FacilityID, FacilityLocation, FacilityName, FacilityThresholder));
							//System.out.println(arrFacility);
						System.out.println(FacilityID + FacilityLocation + FacilityName + FacilityThresholder + FacilityEK);
						ClientUI.clientController.arrFacility.add(new Facility(FacilityID,FacilityArea, FacilityLocation, FacilityName, FacilityThresholder, FacilityEK));
					}		
					awaitResponse = false;
			}
			else
			{// the Else here is must or we get into a dead end.
				clientConsole.display(msg);
			}
			
		}
		awaitResponse = false;
	}
	/***
	 * @param message
	 * Function Sending from third layer of client ( this level ) to the server a message with a type of object.
	 */
	public void handleMessageFromClientUI(Object message) 
	{
		try 
		{
			if(message instanceof RequestObjectClient)
			{
				awaitResponse = true;
				if(((RequestObjectClient)message).getRequestID().equals("#USER_LOGOUT"))
					awaitResponse = false;
				System.out.println("Sending object "+ ((RequestObjectClient)message).getRequestID());
			}
			
			openConnection();
			sendToServer(message);
			while (awaitResponse) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}