package client;

import java.io.IOException;

import Entity.User;
import common.ChatIF;
import common.IController;
import common.SceneManager;

public class ClientConsole implements ChatIF 
{
	public static int DEFAULT_PORT = 5555;
	ChatClient client;
	private User clientUser = new User(null,null);
	SceneManager sceneManager = new SceneManager();
	IController currentController;
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
	public User getUser()
	{
		return clientUser;
	}
	public void setUser(User user)
	{
		clientUser = user;
	}
	public void setController(IController currentController) {
		this.currentController = currentController;
		this.currentController.updatedata(null);
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
		currentController.updatedata(message);
	}

	@Override
	public void setButtons(boolean isConnected) {
		//clientInterface.setPanesAfterSearch(isConnected);
	}

}
