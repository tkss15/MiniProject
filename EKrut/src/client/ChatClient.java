package client;

import java.io.IOException;
import java.util.HashMap;

import gui.ClientInterfaceController;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient {

	public static boolean awaitResponse = false;
	private ChatClient chatClient;
	private ClientInterfaceController clientInterfaceController;

	public ChatClient(String host, int port,ClientInterfaceController clientInterfaceController) throws IOException 
			{
		    super(host, port); //Call the superclass constructor
		    chatClient = this;
		    this.clientInterfaceController = clientInterfaceController;

		  }

	@Override
	protected void handleMessageFromServer(Object msg) {
		if(msg instanceof HashMap) {
			HashMap<String,String> subscriberDetails = (HashMap<String,String>)msg;
			System.out.println(subscriberDetails.toString() + "From Client");
			clientInterfaceController.writeToClientTextArea(subscriberDetails);
		}
	}

	public void handleMessageFromClientUI(Object message) {
		try {
		    openConnection();
			awaitResponse = true;
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