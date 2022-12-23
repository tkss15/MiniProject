package ServerHandlers;

import java.io.IOException;
import java.util.HashMap;

import common.ChatIF;
import common.MessageHandler;
import database.DBConnect;
import ocsf.server.ConnectionToClient;

public class SearchUserHandler implements MessageHandler 
{
	DBConnect mySqlConnection;
	ChatIF serverUI;
	
	public SearchUserHandler(DBConnect mySqlConnection,ChatIF serverUI)
	{
		this.mySqlConnection = mySqlConnection;
		this.serverUI = serverUI;
	}

	@Override
	public void handle(Object msg, ConnectionToClient client) 
	{
		if(msg instanceof String)
		{
			String message = (String) msg;
			serverUI.display("Client( " + client + " ):" + message);
			try 
			{
				HashMap<String,String> data = mySqlConnection.searchUserInDB(mySqlConnection.getConn(), message);
				System.out.println(data);
				client.sendToClient(data);
				serverUI.display("Subscriber sent back from server to client");
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
