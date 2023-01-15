package gui;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.Product;
import Entity.RegisterClient;
import client.ClientConsole;
import client.ClientUI;
import common.CountdownOrder;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import common.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


/***
 * HomePageController is the Controller of the UI Homepage
 */
public class HomePageController implements Initializable, IController
{
	boolean isDefaultUser = false; // A boolean in order to detect if the user is a register client ( Subscriber, Approved or Pending) or not. 
	
	@FXML
	private Text textUserlogin,textSubscriberNumber, textMonthlyFee,textFirstName, textLastName, textStatus, textID, textTelephone, textEmail;
    @FXML
    private VBox subscriberVBox,SubscriberAd;
    @FXML
    private Button Logout, ManageOrders,viewCatalog,BtnCreateOrder,orderPickUp, CloseButton,joinSubscriber;
    
    /***
     * @param event - recives the action event after user clicks
     * openOrderPickup function opens a new scene on the user screen.
     * 
     */
    @FXML
    void openOrderPickup(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowSceneNew("../views/OrderPickup.fxml");// ShowSceneNew - opens a new stage with given fxml file.
    }
    /***
     * actionViewCatlog function switches current client scene with the given scene.
     * the function using ShowScene.
     * @param event
     * 
     * A more detailed explanation about sceneManager in common/SceneManager.java
     */
    @FXML
    void actionViewCatlog(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/CatalogViewerOnly.fxml",event);
    }
    /***
     * openManageOrders function switches current client scene with the given scene.
     * the function using ShowSceneNew.
     * @param event
     * 
     * A more detailed explanation about sceneManager in common/SceneManager.java
     */
    @FXML
    void openManageOrders(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowSceneNew("../views/myOrders.fxml",event);
    }
    /***
     * ShowSubscriberRequest function switches current client scene with the given scene.
     * the function using ShowSceneNew.
     * @param event
     * 
     * A more detailed explanation about sceneManager in common/SceneManager.java
     */
    @FXML
    void ShowSubscriberRequest(ActionEvent event) 
    {
    	RegisterClient temp = (RegisterClient)ClientUI.clientController.getUser();
    	if(!temp.getClientStatus().equals(RegisterClient.ClientStatus.CLIENT_APRROVED))
    	{
        	return;
    	}
    	RequestObjectClient request = new RequestObjectClient(
    			"#UPDATE_CLIENT_TO_BE_SUBCRICBER",
    			String.format("%s#%s#%s#%s#%s#%s#%s#%s#", 
    					temp.getFirstName(),
    					temp.getLastName(),
    					temp.getPhone(),
    					temp.getEmail(),
    					temp.getID(),
    					temp.getUserName(),
    					temp.getPassword(),
    					temp.getArea())
    			,"POST");  
    	ClientUI.clientController.accept(request);
    	
    	request = new RequestObjectClient(
    			"#UPDATE_CLIENT_STATUS",
    			String.format("%s#", 
    					temp.getUserName())
    			,"PUT"); 
    	ClientUI.clientController.accept(request);
    	
    	SubscriberAd.setVisible(false);
    	temp.setClientStatus("Registered To Subscriber");
    	ClientUI.clientController.setUser(temp);
		textStatus.setText("Subscriber Pending\n(Approved)");			
    }
    /***
     * @param event
     * Logout function uses UserDissconnected which sends a request to the server in order to disconnect the client from his user.
     * the function redirects the user to the Login page.
     */
    @FXML 
    void Logout(ActionEvent event)
    {
    	ClientUI.clientController.UserDisconnected(false);
    	ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
    }
    /***
     * @param event
     * closeWindow function uses UserDissconnected(true) which operates the same as UserDissconnected but also closes the windows at the end.
     */
    @FXML
    void closeWindow(ActionEvent event) 
    {
    	ClientUI.clientController.UserDisconnected(true);
    }
    /***
     * @param event
     * openCatalogProduct function is divided into 2 main options
     * Option 1: opening the catalog as an EK - Client who opens the catalog as an EK will be redirect directly into the Catalog page.
     * Option 2: opening the catalog as an OL - redirects the client over to the Order settings page so he will be able to choose the location
     * of facility he wants to order from.
     */
    @FXML
    void openCatalogProduct(Event event) 
    {
    	/*
    	 * Checks if the Current computer is an EK. if on first install the client choose EK then the software will remember that the client
    	 * is An EK.
    	 * */
    	if(ClientUI.clientController.getEKFacility().isFacilityEK())
    	{
    		
        	ClientUI.clientController.getClientOrder().setFacilityType("EK"); // Setting the Client Order to be EK type.
        	ClientUI.clientController.getClientOrder().setOrderFacility(ClientUI.clientController.getEKFacility());// Update the OrderFacility to be the current EK Facility.
        	ClientUI.clientController.getClientOrder().setOrderType("Instant Pickup");// Setting the Order type to be InstantPickup -> Since is an EK machine it will always be Instant Pickup.
        	// User Request from Server data about the facilitys. 
        	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",String.format("%d#", ClientUI.clientController.getEKFacility().getFacilityID()),"*");  
        	ClientUI.clientController.accept(request);
        	
        	// Creating a new Countdown for order termination. if the countdown ends the order will be terminated automaticly.
        	ClientUI.clientController.setTaskCountdown(new CountdownOrder());
    		ClientUI.sceneManager.ShowSceneNew("../views/CatalogViewer.fxml", event);// Switching the clients current scene to CatalogViewer Scene.
    	}
    	else 
    	{// Client is Not using EK. which means he uses the Application from home. 
    		ClientUI.sceneManager.ShowSceneNew("../views/ordersettings.fxml", event);
    	}
    }

	/***
	 * initialize - sets the first values in the controller.
	 * @param arg0 
	 * @param arg1
	 * both @param not used.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		// Client Controller changes in every single UI. we need to update the Client Controller in initialize
		ClientUI.clientController.setController(this);
		
		
		/**
		 * Updates the Welcome back section data.
		 * */
		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());
		
		// Sets the On Click Even of BtnCreateOrder to open the CatalogProduct.
		BtnCreateOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			openCatalogProduct(event);
		});
		
		// Client Request data from the server to check his status as register client
		RequestObjectClient request = new RequestObjectClient("#CHECK_CLIENT_STATUS",String.format("%s#", ClientUI.clientController.getUser().getUserName()),"GET");
		ClientUI.clientController.accept(request);
		
		subscriberVBox.setVisible(false);// subscriberVBox is an information box contains data about the subsrciber it's visable is false since it will appear to subsricber only.
		SubscriberAd.setVisible(false);// Sets the Subscriber Ad off. later we will show only RegisterClient with Approve status the Ad

		if(isDefaultUser)// Client will be a default user only if the data returned from #CHECK_CLIENT_STATUS is none.
		{// Default user has no permissions and cant do any opreation.
			 ManageOrders.setDisable(true);
			 viewCatalog.setDisable(true);
			 BtnCreateOrder.setDisable(true);
			 orderPickUp.setDisable(true);
		}
		else
		{
			// In case the client isnt a default user 
			RegisterClient temp = (RegisterClient)ClientUI.clientController.getUser();
			if(temp.getClientStatus().equals(RegisterClient.ClientStatus.CLIENT_SUBSCRIBER))
			{
				/***
				 * When a Subscriber client opens his homepage his Monthly fee( the amount of money he needs to pay for his delayed payments ) will be displayed
				 * we using Calendar in order to get the current month and calcualte the amount of money needed.
				 */
				Calendar CalenderTime = Calendar.getInstance();// Creating a new instance of Calander
				SimpleDateFormat simpleFormat = new SimpleDateFormat("MM");// We create a template for the date. the template will return only month
				String timeStamp = simpleFormat.format(CalenderTime.getTime());// Saving the returned Month in String.
				
				// Client request data from server about his Monthly fee. the data returned will contain the amount of fee the client has in his account
				request = new RequestObjectClient("#GET_MONTHLY_FEE",String.format("\"%s\"#\"%s\"#",timeStamp,temp.getUserName()),"*");
				ClientUI.clientController.accept(request);
					
				textSubscriberNumber.setText(temp.getClientSubscriberNumber()+"");// Updates the Subscriber number
				textMonthlyFee.setText(String.format("%.2f", temp.getClientMonthlyFee()));// Updates the Monthly fee
				subscriberVBox.setVisible(true);// Sets the Subscriber VBox on so the Subscriber client can see his Information
			}
			// In case the client status is Pending he can only see the Products Catalog.
			if(temp.getClientStatus().equals(RegisterClient.ClientStatus.CLIENT_PENDING))
			{
				 BtnCreateOrder.setDisable(true);
				 orderPickUp.setDisable(true);
				 ManageOrders.setDisable(true);
			}
			if(temp.getClientStatus().equals(RegisterClient.ClientStatus.CLIENT_APRROVED))
			{
				SubscriberAd.setVisible(true);
			}
		}
		// If current Computer is not an EK computer. it means the client is from home so he is not able to pick up orders.
		if(!ClientUI.clientController.getEKFacility().isFacilityEK())
		{
			orderPickUp.setDisable(true);
		}
	}
	/***
	 * HomepageController Updatedata
	 * @param data - data will always be an instance of ResponseObject. data arrives from ClientConsole that arrives from ChatClient
	 * Update data will recive data from a thread which is not the Javafx Application thread.
	 * so when receiving data we save it and then on the application console change it
	 * 
	 */
	@Override
	public void updatedata(Object data) 
	{
		if(data instanceof ResponseObject)// Safety check
		{
			ResponseObject serverResponse = (ResponseObject) data; 
			// ServerResponse will always have the same request as the user sends in ClientRequestObject.
			switch(serverResponse.getRequest())
			{	
				//if the Client Asks for #GET_MONTHLY_FEE this is the data they receive from Server.
				case"#GET_MONTHLY_FEE":
				{
					Object[] values =(Object[]) serverResponse.Responsedata.get(0);// Responsedata is an arraylist of Object arrays
					
					Double MonthlyFee = (Double)values[0];// first value is the Monthly fee recives from server
					RegisterClient temp = (RegisterClient)ClientUI.clientController.getUser();// Updates the monthly fee.
					temp.setClientMonthlyFee(MonthlyFee);
					break;
				}
				//if the Client Asks for #CHECK_CLIENT_STATUS this is the data they receive from Server.
				case"#CHECK_CLIENT_STATUS":
				{
					// If the client has no client status. -> he is a default user and has no permissions. 
					if(serverResponse.Responsedata.size() == 0)// if the size of the responsedata is 0 no data returned from sever.
					{
						isDefaultUser = true;
						textStatus.setText("Default User");
						return;	
					}
					Object[] values =(Object[]) serverResponse.Responsedata.get(0);//Row 1 
					String userStatus = (String) values[1];
					String CardNumber = (String) values[2];
					String CardDate = (String) values[3];
					
					// Sets the Client to be a registerclient with credit card and carddate
					RegisterClient registerClient = new RegisterClient(ClientUI.clientController.getUser(), null, null, CardNumber, CardDate,null);
					
					registerClient.setClientStatus(userStatus);
					// Checks if the current userstatus is subscriber then we load the rest of the data
					if(userStatus.equals("SUBSCRIBER"))
					{
						Integer SubscriberNumber = (Integer) values[4];
						Double MonthlyFee = (Double) values[5];
						int firstPurchase = (int) values[6];	
						
						registerClient.setClientSubscriberNumber(SubscriberNumber);
						registerClient.setClientFirstPurchase(firstPurchase);
						registerClient.setClientMonthlyFee(MonthlyFee);
					}
					ClientUI.clientController.setUser(registerClient);// Sets the new register client as the Client user.
					isDefaultUser = false;// Since the client is not a default user we set this as false.
					textStatus.setText(userStatus);		
					if(userStatus.equals("Registered To Subscriber"))
					{
						textStatus.setText("Subscriber Pending\n(Approved)");
					}
					break;
				}
				//if the Client Asks for #SIMPLE_REQUEST this is the data they receive from Server.
				case"#SIMPLE_REQUEST":
				{
					// this request updates the client available facility products
					ClientUI.clientController.getArrProducts().clear();
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);// gets all the values in a single row
						Integer ProductCode = (Integer) values[0];
						String ProductName = (String) values[1];
						Double ProductPrice = (Double) values[2];
						String ProductDesc = (String) values[3];
						String ProductSrc = (String) values[4];
						Integer ProductAmount = (Integer) values[6];
	
						
						byte[] arrayByte = serverResponse.ResponsePicture.get(i);// Saving pictures in an arraylist of byte arrays
						Product newProduct = new Product(ProductCode,ProductName,ProductDesc, ProductSrc, ProductPrice, ProductAmount);
						
						if(newProduct.PicturePhoto.exists())// Checks if the newProduct have a matching photo in the EkrutApplication directory
						{
							(ClientUI.clientController.getArrProducts()).add(newProduct);// if there is a matching photo then we add it.
							continue;
						}
						FileOutputStream fos;
						try 
						{
							fos = new FileOutputStream(newProduct.PicturePhoto);
							BufferedOutputStream bos = new BufferedOutputStream(fos); /* Create BufferedFileOutputStream */
							
							bos.write(arrayByte, 0, arrayByte.length); /* Write byte array to output stream */
		
							// flushing the bos and fos after every picture and closing the fos and bos.
						    bos.flush();
						    fos.flush();
						    
						    bos.close();
						    fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /* Create file output stream */
						(ClientUI.clientController.getArrProducts()).add(newProduct);// Adding the product to the facility products in the clientconsole.
					}
					break;
				}
			}
		}
	}

}

