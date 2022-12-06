package client;

import java.io.IOException;
import java.util.HashMap;

import common.ChatIF;
import gui.ClientInterfaceController;



public class ClientConsole implements ChatIF  
{
	public static int DEFAULT_PORT = 5555 ;
	ChatClient client;
	private ClientInterfaceController clientInterface;
	
	public ClientConsole(String host, int port, ClientInterfaceController clientInterface) {
		try 
	    {
		  this.clientInterface = clientInterface;
	      client = new ChatClient(host, port,this);
	      System.out.println("A new client connected to port:" + port);
	    } 
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!Terminating client.");
	      System.exit(1);
	    }
	}
	
	public void accept(Object msg) 
	{
		client.handleMessageFromClientUI(msg);
	}
	
	@Override
	public void display(Object message) {
		if(message instanceof HashMap) {
			 clientInterface.setPanesAfterSearch(true);
			 clientInterface.writeToClientTextArea(message);
		}
		else 
		{
			if(message.equals("#errornoid"))
			{
				clientInterface.setPanesAfterSearch(false);
				clientInterface.getMissingIDField().setVisible(false);
				clientInterface.getIDNotFoundMSG().setVisible(true);
				
				
			}
			else 
			{
				clientInterface.writeToClientTextArea((String)message);
			}
		}
	}

	@Override
	public void setButtons(boolean isConnected) {
		clientInterface.setPanesAfterSearch(isConnected);
	}
	

}
