package client;

import java.io.IOException;
import java.util.HashMap;

import common.ChatIF;
import gui.ClientInterfaceController;

public class ClientConsole implements ChatIF 
{
	public static int DEFAULT_PORT = 5555;
	ChatClient client;
	/***
	 * ClientConsole is the Second layer of client. client console works with the ClientInterfaceController and with ChatClient.
	 * @param host - saves the ip-address string adress that allows client to connect to sever.
	 * @param port - saves the port integer that allows client to connect to server.
	 * @param clientInterface
	 */
	public ClientConsole(String host, int port) 
	{
		try 
		{
			client = new ChatClient(host, port, this);
		} 
		catch (IOException exception) 
		{
			System.out.println("Error: Can't setup connection!Terminating client.");
			System.exit(1);
		}
	}
	/***
	 * @param msg
	 * Function operates as second layer. the function sends object(msg) to the @ChatClient
	 * when client wants to send a message to the server. the message advanced to the next layer until it sends to the server.
	 * 										  We are here.
	 * Client UI -> Client UI Controller -> Client Console -> ChatClient -> EchoServer
	 */
	public void accept(Object msg) 
	{
		client.handleMessageFromClientUI(msg);
	}
	
	/***
	 * @param message
	 * 
	 * 
	 */
	@Override
	public void display(Object message) 
	{
//		if (message instanceof HashMap) 
//		{
//			clientInterface.setPanesAfterSearch(true);
//			clientInterface.writeToClientTextArea(message);
//		} 
//		else 
//		{
//			String msg = (String) message;
//			if (msg.startsWith("#ErrorSub")) {
//				String[] messageToClient = msg.split("#ErrorSub");
//				clientInterface.UpdateMessage(messageToClient[1], false);
//			} else if (msg.startsWith("#SucssSub")) {
//				String[] messageToClient = msg.split("#SucssSub");
//				clientInterface.UpdateMessage(messageToClient[1], true);
//			}
//			if (message.equals("#errornoid")) {
//				clientInterface.setPanesAfterSearch(false);
//				clientInterface.getMissingIDField().setVisible(false);
//				clientInterface.getIDNotFoundMSG().setVisible(true);
//			} 
//			else 
//			{
//				clientInterface.writeToClientTextArea((String) message);
//			}
//		}
	}

	@Override
	public void setButtons(boolean isConnected) {
		//clientInterface.setPanesAfterSearch(isConnected);
	}

}