package Server;

import java.util.ArrayList;

import common.ChatIF;
import common.ClientConnection;
import common.IController;
import gui.ServerInterfaceController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerConsole implements ChatIF 
{
	
	IController serverInterface;
	EchoServer server;
	final public static int DEFAULT_PORT = 5555;
	
	/**
	 * 
	 * @param port the number of the port for connection.
	 * @param serverInterface 
	 */
	public ServerConsole(int port, IController serverInterface)
	{
		server = new EchoServer(port,this);
		this.serverInterface = serverInterface;
	}

 
	/**
	 * 
	 * @param message object sent from the serverUI.
	 */
	public void accept(Object message)
	{
		try
		{
			server.handleMessageFromServerUI(message);
		} 
		catch (Exception ex) 
		{
			System.out.println
			("Unexpected error while reading from console!");
		}
	}
	@Override
	/**
	 * updates the data of the controller.
	 */
	public void display(Object message)
	{
		serverInterface.updatedata(message);
		
    }

}
