package Server;

import common.ChatIF;
import common.ClientConnection;
import gui.ServerInterfaceController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServerConsole implements ChatIF 
{
	
	ServerInterfaceController serverInterface;
	EchoServer server;
	final public static int DEFAULT_PORT = 5555;
	ObservableList<ClientConnection> colums = FXCollections.<ClientConnection>observableArrayList();
	
	public ServerConsole(int port, ServerInterfaceController serverInterface)
	{
		server = new EchoServer(port,this);
		this.serverInterface = serverInterface;
	}

 
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
	public void display(Object message)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				if(message instanceof ClientConnection)
				{
					ClientConnection clientConnectionMessage = (ClientConnection)message;
					
					if(colums.contains(clientConnectionMessage))
					{
						colums.remove(clientConnectionMessage);
					}
					colums.add(clientConnectionMessage);
					serverInterface.getTable().setItems(colums);
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
