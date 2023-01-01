package client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import Entity.User;
import common.ChatIF;
import common.MyFile;
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
		if(msg instanceof MyFile)
		{
			  int fileSize =((MyFile)msg).getSize(); 
			  System.out.println("Message received: " + msg + " from Server");
			  System.out.println("length "+ fileSize);
			  MyFile clientFile = (MyFile) msg;
			  
			  try 
			  {
				byte[] mybytearray = clientFile.getMybytearray();
				File newDir = new File("C:\\EkrutApplication\\pictures");
				newDir.mkdirs();
				File newFile = new File("C:\\EkrutApplication\\pictures\\"+ clientFile.getFileName());
				System.out.println(clientFile.getFileName());
				FileOutputStream fos = new FileOutputStream(newFile); /* Create file output stream */
				BufferedOutputStream bos = new BufferedOutputStream(fos); /* Create BufferedFileOutputStream */
				bos.write(mybytearray, 0, clientFile.getSize()); /* Write byte array to output stream */
				System.out.println(bos);
			    bos.flush();
			    fos.flush();
			  }
			  catch (Exception e) 
			  {
				System.out.println("Error uploading (Files)msg) to Server");
				e.printStackTrace();
			  }
		}
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