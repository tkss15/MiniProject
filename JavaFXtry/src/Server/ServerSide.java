package Server;

import java.io.IOException;

import Database.DBConnect;
import gui.ServerInterfaceController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerSide extends AbstractServer
{
	final public static int DEFAULT_PORT = 5555;
	ServerInterfaceController ServerController;
	DBConnect mySqlConnection;
	public ServerSide(int port, ServerInterfaceController ServerController) 
	{
		super(port);
		this.ServerController = ServerController;
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) 
	{
		// TODO Auto-generated method stub
		
	}
	protected void serverStarted()
	{
		ServerController.writeToConsole("Server Started at Port 5555");
		mySqlConnection = new DBConnect(ServerController, ServerController.getTextboxDBUserName().getText(), ServerController.getTextPasswordF().getText(), ServerController.getTextboxDBName().getText());
		mySqlConnection.connectToDB();
		ServerController.getConnectLogo().setVisible(false);
		ServerController.getConnectButton().setDisable(true);
		ServerController.getDissconnectButton().setDisable(false);
		//SendMessageToConsoleUI();
	}
	
	protected void serverStopped()
	{
		ServerController.writeToConsole("Server Stopped" );
		ServerController.getConnectButton().setDisable(false);
		ServerController.getDissconnectButton().setDisable(true);
		ServerController.getConnectLogo().setVisible(true);
		//mySqlConnection.disconnectFromDB();
	}

}
