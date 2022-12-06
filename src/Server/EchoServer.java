package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

import client.ClientConnection;
import common.ChatIF;
import database.DBConnect;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer 
{
	ChatIF serverUI;
	DBConnect mySqlConnection;
	public EchoServer(int port) 
	{
		super(port);
		// TODO Auto-generated constructor stub
	}
	  
	public EchoServer(int port,ChatIF serverUI) 
	{
		super(port);
	    this.serverUI = serverUI;
	}
	boolean isServerClosed;
	protected void serverStarted()
	{
		serverUI.display("Server listening for connections on port " + getPort());
		mySqlConnection = new DBConnect(serverUI);
		mySqlConnection.connectToDB();
		serverUI.setButtons(true);
	    isServerClosed = false;
	}
	  
	  /**
	   * This method overrides the one in the superclass.  Called
	   * when the server stops listening for connections.
	   */
	protected void serverStopped()
	{
		serverUI.display("Server has stopped listening for connections.");
	  System.out.println
	    ("Server has stopped listening for connections.");
	  isServerClosed = true;
	}
	
	
	
	public void handleMessageFromServerUI(String message)
	{
		  if(message.charAt(0) == '#')
		  {
				try{
					handleServerCommands(message);
				}
				catch(IOException e)
				{
					serverUI.display(e.getMessage());
				}
		  }
		  else
		  {
			  serverUI.display("SERVER MSG>" + message);
		  }
	}
	private void handleServerCommands(String message) throws IOException
	{
		switch(message)
		{
			case"#close": close();
			serverUI.setButtons(false);
			break;
		}
	}
	protected void clientConnected(ConnectionToClient client) 
	{
		ClientConnection clientToShow = new ClientConnection(client);
		serverUI.display("Client Connected");
		serverUI.display(clientToShow);
	}
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) 
	{
		if((msg instanceof HashMap))
		{
			HashMap<String,String> data = (HashMap<String,String>) msg;
			mySqlConnection.updateUserToDB(mySqlConnection.getConn(),data);
			try 
			{
				client.sendToClient("Server updated sucssfully");
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			serverUI.display("Client(" + client + "): Request to update" + msg);
		}
		else if((msg instanceof String)) 
		{
			serverUI.display("Client( " + client + " ):" + msg);
			try 
			{
				client.sendToClient(mySqlConnection.searchUserInDB(mySqlConnection.getConn(),msg.toString()));
				serverUI.display("Subscriber sent back from server to client");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

}
