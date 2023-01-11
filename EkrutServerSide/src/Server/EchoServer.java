package Server;

import java.io.IOException;
import java.util.ArrayList;

import com.twilio.Twilio;

import common.ChatIF;
import common.ClientConnection;
import common.RequestObjectClient;
import common.ResponseObject;
import database.DBConnect;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer 
{
	ChatIF serverUI;
	DBConnect mySqlConnection;
	private ArrayList<String> serverConfing;
	
	public EchoServer(int port) 
	{
		super(port);
	}

	public EchoServer(int port, ChatIF serverUI) 
	{
		this(port);
		this.serverUI = serverUI;
	}

	boolean isServerClosed;
	protected void serverStarted() 
	{
		serverUI.display("Server listening for connections on port " + getPort());
		serverUI.display("#SetButtonsOn");
		isServerClosed = false;
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() 
	{
		serverUI.display("Server has stopped listening for connections.");
		isServerClosed = true;
	}
	
	@SuppressWarnings("unchecked")
	public void handleMessageFromServerUI(Object message) {
		if(message instanceof ArrayList)
		{
			serverConfing = (ArrayList<String>)message;
			serverUI.display("DASDASDAS" + serverConfing.size());
			if(serverConfing.size() == 7)
			{
				try 
				{
					Twilio.init(serverConfing.get(5), serverConfing.get(6));
					//SMSNotifiction sendExample = new SMSNotifiction("0547443546");
					//sendExample.SendNotification("הדגמה");
				}
				catch(Exception error)
				{
					serverUI.display(error.getMessage());
				}
			}
			mySqlConnection = new DBConnect(serverUI, serverConfing);
			mySqlConnection.connectToDB();
		}
		else if (message instanceof String) 
		{
			String strMessage = (String) message;
			if (strMessage.charAt(0) == '#') 
			{
				try 
				{
					handleServerCommands(strMessage);
				} catch (IOException e) 
				{
					serverUI.display(e.getMessage());
				}
			}
		}
	}

	private void handleServerCommands(String message) throws IOException 
	{
		switch (message) 
		{
			case "#close":
				close();
				serverUI.display("#SetButtonsOff");
			break;
		}
	}
	protected void clientDisconnected(ConnectionToClient client) 
	{
		ClientConnection clientToShow = new ClientConnection(client,false);
		serverUI.display("Client Disconnected "+ client.getInetAddress());
		serverUI.display(clientToShow);
	}
	protected void clientConnected(ConnectionToClient client) 
	{
		//SendPhotosToClient(null,client);
		ClientConnection clientToShow = new ClientConnection(client);
		serverUI.display("Client Connected "+ client.getInetAddress());
		serverUI.display(clientToShow);
	}
	//#SEND_SMS
	//#SEND_EMAIL
	
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) 
	{
		if(msg instanceof RequestObjectClient)
		{
			RequestObjectClient clientRequest = (RequestObjectClient) msg;
			try 
			{
				
				/*
				 * Explantion:
				 * 
				 * - Client Request ends with SEND_ALL it will update all users.
				 * - Client Request ends with SEND_NOT_ME it will update all users BUT the sender. ( it will send empty response ).
				 * - Other.
				 * */
				if(clientRequest.getRequestID().endsWith("#SEND_NOT_ME"))
				{
					clientRequest.setRequestID(clientRequest.getRequestID().replace("#SEND_NOT_ME", ""));
					ResponseObject ResponseEmpty = new ResponseObject("Empty");
					ResponseEmpty.setRequest("Empty");
					
					sendToAllClients(mySqlConnection.SafeQuery(clientRequest), client);
					client.sendToClient(ResponseEmpty);
				}
				if(clientRequest.getRequestID().endsWith("#SEND_ALL"))
				{
					clientRequest.setRequestID(clientRequest.getRequestID().replace("#SEND_ALL", ""));
					sendToAllClients(mySqlConnection.SafeQuery(clientRequest));
				}
				
				
				if(clientRequest.getRequestID().equals("#USER_LOGOUT"))
				{
					clientDisconnected(client);
					client.close();
				}
				else 
				{
					client.sendToClient(mySqlConnection.SafeQuery(clientRequest));
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
