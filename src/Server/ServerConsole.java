package Server;

import client.ClientConnection;
import common.ChatIF;
import gui.ServerInterfaceController;
import javafx.application.Platform;

public class ServerConsole implements ChatIF {
	
	ServerInterfaceController serverInterface;
	EchoServer server;
	final public static int DEFAULT_PORT = 5555;
	
	public ServerConsole(int port, ServerInterfaceController serverInterface){
		server = new EchoServer(port,this);
		this.serverInterface = serverInterface;
	}

 
	public void accept(String message)
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
	public void display(Object message)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				if(message instanceof ClientConnection)
				{
					ClientConnection c = (ClientConnection)message;
					System.out.println("OK");
					serverInterface.getTable().getItems().add(c);
					serverInterface.getTable().refresh();
				}
				else
					serverInterface.writeToConsole((String)message);
			}
		});
    }
	@Override
	public void setButtons(boolean isConnected)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				serverInterface.getConnectLogo().setVisible(!isConnected);
				serverInterface.getConnectButton().setDisable(isConnected);
				serverInterface.getDissconnectButton().setDisable(!isConnected);
			}
		});
	}

}
