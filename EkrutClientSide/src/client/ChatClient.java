package client;

import java.io.IOException;
import java.util.HashMap;

import Entity.User;
import common.ChatIF;
import common.RequestObjectClient;
import common.ResponseObject;
import common.SceneManager;
import javafx.application.Platform;
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
//		if (msg == null) 
//		{
//			clientConsole.display("#errornoid");
//		} else if (msg instanceof Boolean) 
//		{
//			clientConsole.display((Boolean) msg ? "#SucssSubData have been updated sucssfully."
//					: "#ErrorSubCould not update Subscriber number. Subscriber number already taken.");
//		} 
		if(msg instanceof ResponseObject)
		{
			clientConsole.display(msg);
		
		}
		else if (msg instanceof HashMap) 
		{
			HashMap<String, String> subscriberDetails = (HashMap<String, String>) msg;
			clientConsole.display(subscriberDetails);
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
				System.out.println("Sending object "+ ((RequestObjectClient)message).getRequestID());
			}
	       	awaitResponse = true;
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