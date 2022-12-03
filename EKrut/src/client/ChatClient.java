package client;

import java.io.IOException;
import java.util.HashMap;

import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient {

	public static boolean awaitResponse = false;

	public ChatClient(String host, int port) throws IOException {
		super(host, port); // Call the superclass constructor
		// openConnection();
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if(msg instanceof HashMap) {
			@SuppressWarnings("unchecked")
			HashMap<String,String> subscriberDetails = (HashMap<String,String>)msg;
			System.out.println(subscriberDetails.toString() + "From Client");
		}
	}

	public void handleMessageFromClientUI(Object message) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(message);
			System.out.println("Massage sent to server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
