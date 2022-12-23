package Server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import common.ChatIF;
import common.MessageHandler;
import database.DBConnect;
import ocsf.server.ConnectionToClient;

public class LoginUserHandler implements MessageHandler 
{
	DBConnect mySqlConnection;
	ChatIF serverUI;
	
	public LoginUserHandler(DBConnect mySqlConnection,ChatIF serverUI)
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
			
			
			Connection conn = mySqlConnection.getConn();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM users WHERE id='%s';"));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
