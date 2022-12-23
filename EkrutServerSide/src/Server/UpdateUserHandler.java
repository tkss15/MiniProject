package Server;

import java.io.IOException;
import java.util.HashMap;

import common.ChatIF;
import common.MessageHandler;
import database.DBConnect;
import ocsf.server.ConnectionToClient;

public class UpdateUserHandler implements MessageHandler 
{
	DBConnect mySqlConnection;
	ChatIF serverUI;
	
	public UpdateUserHandler(DBConnect mySqlConnection,ChatIF serverUI)
	{
		this.mySqlConnection = mySqlConnection;
		this.serverUI = serverUI;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void handle(Object msg, ConnectionToClient client) 
	{
		HashMap<String, String> data = (HashMap<String, String>) msg;
		try 
		{
			client.sendToClient(mySqlConnection.updateUserToDB(mySqlConnection.getConn(), data));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		serverUI.display("Client(" + client + "): Request to update" + msg);
	}

}
