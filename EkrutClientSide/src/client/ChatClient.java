package client;

import java.io.IOException;
import java.util.Optional;

import Entity.Facility;
import Entity.Order;
import Entity.Product;
import common.ChatIF;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient 
{
	public static boolean awaitResponse = false;
	ChatIF clientConsole;
	/***
	 * @param host - saves the data of the ip-address the client entered in order to connect to the server.
	 * @param port - saves the data of the port the client entered in order to connect to the server.
	 * @param clientUI - interface that allows the upper layer of client to communicate with lower layers.
	 * @throws IOException
	 * 
	 *ChatClient is a class that communicate directly with the server. is the highest layered class and closest to the sever in terms of layering.
	 */
	public ChatClient(String host, int port, ChatIF clientConsole) throws IOException 
	{
		super(host, port); // Call the superclass constructor
		this.clientConsole = clientConsole;
		openConnection();
		sendToServer(getInetAddress());
	}
	
	/***
	 * @param msg
	 * Function handling @msg that client receives from the server.
	 * every message coming from the server is an object.
	 */
	@Override
	protected void handleMessageFromServer(Object msg) 
	{
		if(msg instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) msg;
			
			switch(serverResponse.getRequest())
			{
				case"Empty":
				{
					awaitResponse = false;
					break;
				}
				case"#FIRST_INSTALL":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						String FacilityArea = (String)values[1];
						String FacilityLocation = (String)values[2];
						String FacilityName = (String)values[3];
						Integer FacilityThresholder = (Integer)values[4];
						Integer FacilityEK = (Integer) values[5];

						ClientUI.clientController.arrFacility.add(new Facility(FacilityID,FacilityArea, FacilityLocation, FacilityName, FacilityThresholder, FacilityEK == 0 ? false : true
						));
					}		
					awaitResponse = false;
					break;
				}
				case"#UPDATE_PRODUCTS_CLIENT":
				{
					clientConsole.display(msg);
					showAlert("Facility Quantity updated!");
					break;
				}
				default:
				{
					clientConsole.display(msg);
					break;
				}
			}
			
		}
		awaitResponse = false;
	}
	public void showAlert(String message) 
	{
	    Platform.runLater(() -> {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setContentText(message);
	        alert.showAndWait();
	    });
	}
	/***
	 * @param message
	 * Function Sending from third layer of client ( this level ) to the server a message with a type of object.
	 */
	public void handleMessageFromClientUI(Object message) 
	{
		try 
		{
			if(message instanceof RequestObjectClient)
			{
				awaitResponse = true;
				if(((RequestObjectClient)message).getRequestID().equals("#USER_LOGOUT"))
					awaitResponse = false;
			}
			
			openConnection();
			sendToServer(message);
			while (awaitResponse) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}