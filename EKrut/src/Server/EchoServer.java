package Server;

import java.io.IOException;

import Database.DBConnect;
import gui.ServerInterfaceController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class EchoServer extends AbstractServer
{
	final public static int DEFAULT_PORT = 5555;
	
	ServerInterfaceController ServerController;
	DBConnect mySqlConnection;
	public EchoServer(int port, ServerInterfaceController ServerController) 
	{
		super(port);
		this.ServerController = ServerController;
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) 
	{
		if(!(msg instanceof String)) {
			throw new IllegalArgumentException("msg was not ID", null);
		}
		System.out.println("Message received: " + msg + " from " + client);
		try {
			client.sendToClient(mySqlConnection.searchUserInDB(mySqlConnection.getConn(),msg.toString()));
			System.out.println("Subscriber sent back from server to client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void serverStarted()
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				ServerController.writeToConsole("Server Started at Port " + ServerController.portStr());
				mySqlConnection = new DBConnect(ServerController);
				mySqlConnection.connectToDB();
				ServerController.getConnectLogo().setVisible(false);
				ServerController.getConnectButton().setDisable(true);
				ServerController.getDissconnectButton().setDisable(false);
			}
		});
		
		
		//SendMessageToConsoleUI();
	}
	
	protected void serverStopped()
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				ServerController.writeToConsole("Server Stopped" );
				ServerController.getConnectButton().setDisable(false);
				ServerController.getDissconnectButton().setDisable(true);
				ServerController.getConnectLogo().setVisible(true);
			}
		});
		
		//mySqlConnection.disconnectFromDB();
	}

}
