package client;

import java.io.IOException;
import java.util.HashMap;

import common.ChatIF;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient {

	public static boolean awaitResponse = false;
	
	ChatIF clientUI;
	
	public ChatClient(String host, int port,ChatIF clientUI) throws IOException 
	{
		    super(host, port); //Call the superclass constructor
		    this.clientUI = clientUI;
		    openConnection();
		    sendToServer(getInetAddress());
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		
		if(msg == null)
		{
			clientUI.display("#errornoid");
		}
		else if(msg instanceof HashMap) {
			HashMap<String,String> subscriberDetails = (HashMap<String,String>)msg;
			System.out.println(subscriberDetails.toString() + "From Client");
			clientUI.display(subscriberDetails);
		}
	}

	public void handleMessageFromClientUI(Object message) 
	{
		try 
		{
			awaitResponse = true;
			openConnection();
			sendToServer(message);
			System.out.println("Massage sent to server");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	  public void quit()
	  {
	    try
	    {
	      closeConnection();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	  }
}