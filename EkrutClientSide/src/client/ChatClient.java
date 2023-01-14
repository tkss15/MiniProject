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
	public static boolean awaitResponse = false; //aslkfsasjfbalsskfna;slfm
	ChatIF clientConsole;
	/***
	 * @param host - saves the data of the ip-address the client entered in order to connect to the server.
	 * @param port - saves the data of the port the client entered in order to connect to the server.
	 * @param clientUI - interface that allows the upper layer of client to communicate with lower layers.
	 * @throws IOException - if connection failed when the client sends its IP to the server then it will throw IOException
	 * 
	 *ChatClient is a class that communicate directly with the server. is the highest layered class and closest to the sever in terms of layering.
	 */
	public ChatClient(String host, int port, ChatIF clientConsole) throws IOException 
	{
		super(host, port); // Call the superclass constructor
		this.clientConsole = clientConsole;// updates the client console 
		openConnection();
		sendToServer(getInetAddress());
	}
	
	/***
	 * @param msg
	 * Function handling @msg that client receives from the server.
	 * every message coming from the server is an object with the type of <strong>ResponseObject</strong>.
	 * the ChatClient sends down the object of the ResponseObject
	 * ChatClient -> ClientConsole -> Controller 
	 * 
	 * Special cases: 
	 * sometimes we will not inform the lower layers( ClientConsole, Controller) if we need to notify the client no matter at what page he is 
	 * ( Global notification ) 
	 * 
	 * A more detail explanation about ResponseObject will be at /common/ResponseObject
	 */
	@Override
	protected void handleMessageFromServer(Object msg) 
	{
		if(msg instanceof ResponseObject) // Messages from server should always be from the type of ResponseObject. is a Safety check
		{
			ResponseObject serverResponse = (ResponseObject) msg;
			
			// Since every client request has a request id. the server returns the same request id so the client can 
			// get the matching data from the request
			switch(serverResponse.getRequest())
			{
				// Special Case "Empty" its a case where the client gets an empty response. this case is useless?
				case"Empty":
				{
					awaitResponse = false;
					break;
				}
				//#Update_areamanager is a case where a client buys a product and the product goes under the threshold level
				// after the client ends his buy it sends a message to the matching area manager.
				case"#UPDATE_AREAMANAGER":
				{
					// AllFacility will write to the Area manager all the facilities under the threshold level.
					StringBuilder AllFacility = new StringBuilder();
					//the data received from the server is the user name of the area managers and the facility that is under the threshold level.
					
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);
						String userName = (String)values[0];// Area manager username
						if(!userName.equals(ClientUI.clientController.getUser().getUserName())) // we want to notify only the correct area manager 
							continue;
						// Gets the facility ID and adds it to the String AllFaility
						Integer FacilityID = (Integer) values[2];
						
						AllFacility.append(FacilityID + " ");
					}
					if(AllFacility.length() > 0)// if the length is 0. the user is not an area manager and should not get an alert.
						showAlert(String.format("Facilitys %s are under the Threshold level", AllFacility.toString()));
				}
				//#UPDATE_PRODUCTS_CLEINT is a case where a client buys a product we want to notify other clients that the products quantity
				// have changed.
				case"#UPDATE_PRODUCTS_CLIENT":
				{
					// the Responsedata holds all the products that were just updated we only want to trasnfer
					//this data to clients that buy right now from the same facility
					if(serverResponse.Responsedata.size() != 0)
					{
						Object[] values = (Object[])serverResponse.Responsedata.get(0);
						Integer FacilityID = (Integer)values[0];// check facility id
						
						// Match only clients that have same facility id in the order
						if(ClientUI.clientController.getClientOrder().getOrderFacility() != null && 
						   ClientUI.clientController.getClientOrder().getOrderFacility().getFacilityID() != FacilityID)
							return;
						// Show update message for them
						clientConsole.display(msg);
						showAlert("Facility Quantity updated!");
					}
					break;
				}
				case "#UPDATE_EST_TIME_FOR_USER":
				{
					if (serverResponse.Responsedata.size() != 0) 
					{
						Object[] values = (Object[]) serverResponse.Responsedata.get(0);
						String userName = (String) values[1];
						
						if (!userName.equals(ClientUI.clientController.getUser().getUserName()))
							return;
						
						int orderCode = (int) values[2];
						String estTime = (String) values[0];
						
						showAlert(String.format("Order %d has been Dispatched and will be received in %s!", orderCode,
								estTime));
					}
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
	/***
	 * 
	 * @param message - will hold a string to show as an information to the client
	 *  ShowAlert will show a popup alert with the same message
	 */
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
	        // If the message is a RequestObjectClient, set the awaitResponse flag and print the request ID
	        if(message instanceof RequestObjectClient)
	        {
	            awaitResponse = true;
	            if(((RequestObjectClient)message).getRequestID().equals("#USER_LOGOUT")) // we don't need to wait for the server response on logout.
	                awaitResponse = false;
	        }
	        // Open a connection to the server
	        openConnection();
	        // Send the message to the server
	        sendToServer(message);
	        // While the awaitResponse flag is set, sleep for 200 milliseconds
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