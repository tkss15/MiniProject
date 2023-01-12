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

public class HomePageController implements Initializable, IController
{
	boolean isDefaultUser = false;
	@FXML
	private Text textUserlogin,textSubscriberNumber, textMonthlyFee,textFirstName, textLastName, textStatus, textID, textTelephone, textEmail;
    
    @FXML
    private VBox subscriberVBox,SubscriberAd;
    
    @FXML
    private Button Logout;

    @FXML
    private Button ManageOrders,viewCatalog,BtnCreateOrder,orderPickUp;

    @FXML
    private Button CloseButton;

    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2;

    @FXML
    void openOrderPickup(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/OrderPickup.fxml");
    }
    @FXML
    void actionViewCatlog(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/CatalogViewerOnly.fxml",event);
    }
    @FXML
    void openManageOrders(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowSceneNew("../views/myOrders.fxml",event);
    }
    @FXML
    void ShowSubscriberRequest(ActionEvent event) {

    }
    @FXML 
    void Logout(ActionEvent event)
    {
		if(ClientUI.clientController.getUser().getOnlineStatus() == null)
		{
			System.out.println("Not updated");
		}
		if(ClientUI.clientController.getUser().getOnlineStatus().equals("Online"))
		{
			
	    	RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",String.format("%s#", ClientUI.clientController.getUser().getUserName()),"PUT");    
	    	ClientUI.clientController.accept(request);
	    	ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
    	ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
    	//System.exit(0);
    }
    @FXML
    void closeWindow(ActionEvent event) 
    {
    	ClientUI.clientController.UserDissconnected();
    	System.exit(0);
    }
    
    @FXML
    void openCatalogProduct(Event event) {
    	System.out.println("Closed");
    	
    	if(ClientUI.clientController.getEKFacility().isFacilityEK())
    	{
        	ClientUI.clientController.getClientOrder().setFacilityType("EK");
        	ClientUI.clientController.getClientOrder().setOrderFacility(ClientUI.clientController.getEKFacility());
        	ClientUI.clientController.getClientOrder().setOrderType("Instant Pickup");
        	//String sql = "SELECT products.*, productsinfacility.ProductAmount FROM products LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode WHERE productsinfacility.FacilityID = " + ClientUI.clientController.getEKFacility().getFacilityID() + " ORDER BY products.ProductCode";
        	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",String.format("%d#", ClientUI.clientController.getEKFacility().getFacilityID()),"*");  
        	ClientUI.clientController.accept(request);
        	
        	ClientUI.clientController.setTaskCountdown(new CountdownOrder());
    		ClientUI.sceneManager.ShowSceneNew("../views/CatalogViewer.fxml", event);
    	}
    	else {
    		ClientUI.sceneManager.ShowSceneNew("../views/ordersettings.fxml", event);
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		ClientUI.clientController.setController(this);
		Calendar CalenderTime = Calendar.getInstance();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("MM");
		String timeStamp = simpleFormat.format(CalenderTime.getTime());
		
		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());
		
		BtnCreateOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			openCatalogProduct(event);
		});
		RequestObjectClient request = new RequestObjectClient("#CHECK_CLIENT_STATUS",String.format("%s#", ClientUI.clientController.getUser().getUserName()),"GET");
		ClientUI.clientController.accept(request);
		
		subscriberVBox.setVisible(false);
		if(isDefaultUser)
		{
			 ManageOrders.setDisable(true);
			 viewCatalog.setDisable(true);
			 BtnCreateOrder.setDisable(true);
			 orderPickUp.setDisable(true);
		}
		else
		{
			RegisterClient temp = (RegisterClient)ClientUI.clientController.getUser();
			if(temp.getClientStatus().equals(RegisterClient.ClientStatus.CLIENT_SUBSCRIBER))
			{
				request = new RequestObjectClient("#GET_MONTHLY_FEE",String.format("\"%s\"#\"%s\"#",timeStamp,temp.getUserName()),"*");
				ClientUI.clientController.accept(request);
				
//				request = new RequestObjectClient("#UPDATE_MONTHLY_FEE",String.format("%s#%.2f#",temp.getUserName(), temp.getClientMonthlyFee()),"PUT");
//				ClientUI.clientController.accept(request);
				
				textSubscriberNumber.setText(temp.getClientSubscriberNumber()+"");
				textMonthlyFee.setText(String.format("%.2f", temp.getClientMonthlyFee()));
				subscriberVBox.setVisible(true);
				SubscriberAd.setVisible(false);
			}
			if(temp.getClientStatus().equals(RegisterClient.ClientStatus.CLIENT_PENDING))
			{
				 BtnCreateOrder.setDisable(true);
				 orderPickUp.setDisable(true);
				 ManageOrders.setDisable(true);
			}
		}
		if(!ClientUI.clientController.getEKFacility().isFacilityEK())
		{
			orderPickUp.setDisable(true);
		}
	}

	@Override
	public void updatedata(Object data) 
	{
		System.out.println("HomePageController");
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
				case"#GET_MONTHLY_FEE":
				{
					Object[] values =(Object[]) serverResponse.Responsedata.get(0);
					Double MonthlyFee = (Double)values[0];
					RegisterClient temp = (RegisterClient)ClientUI.clientController.getUser();
					temp.setClientMonthlyFee(MonthlyFee);
					break;
				}
				case"#CHECK_CLIENT_STATUS":
				{
					if(serverResponse.Responsedata.size() == 0)
					{
						isDefaultUser = true;
						textStatus.setText("Default User");
						return;	
					}
					Object[] values =(Object[]) serverResponse.Responsedata.get(0);//Row 1 
					String userStatus = (String) values[1];
					String CardNumber = (String) values[2];
					String CardDate = (String) values[3];
					
					RegisterClient registerClient = new RegisterClient(ClientUI.clientController.getUser(), null, null, CardNumber, CardDate,null);
					
					registerClient.setClientStatus(userStatus);
					if(userStatus.equals("SUBSCRIBER"))
					{
						Integer SubscriberNumber = (Integer) values[4];
						Double MonthlyFee = (Double) values[5];
						boolean firstPurchase = (boolean) values[6];	
						
						registerClient.setClientSubscriberNumber(SubscriberNumber);
						registerClient.setClientFirstPurchase(firstPurchase);
						registerClient.setClientMonthlyFee(MonthlyFee);
					}
					ClientUI.clientController.setUser(registerClient);
					isDefaultUser = false;
					System.out.println(registerClient);
					textStatus.setText(userStatus);
					
					break;
				}
				case"#SIMPLE_REQUEST":
				{
					ClientUI.clientController.getArrProducts().clear();
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);//Row 1 
						Integer ProductCode = (Integer) values[0];
						String ProductName = (String) values[1];
						Double ProductPrice = (Double) values[2];
						String ProductDesc = (String) values[3];
						String ProductSrc = (String) values[4];
						Integer ProductAmount = (Integer) values[6];
	
						
						byte[] arrayByte = serverResponse.ResponsePicture.get(i);// Picture's
						Product anotherProduct = new Product(ProductCode,ProductName,ProductDesc, ProductSrc, ProductPrice, ProductAmount);
						if(anotherProduct.PicturePhoto.exists())
						{
							(ClientUI.clientController.getArrProducts()).add(anotherProduct);
							continue;
						}
						FileOutputStream fos;
						try {
							fos = new FileOutputStream(anotherProduct.PicturePhoto);
							BufferedOutputStream bos = new BufferedOutputStream(fos); /* Create BufferedFileOutputStream */
							
							bos.write(arrayByte, 0, arrayByte.length); /* Write byte array to output stream */
							System.out.println(anotherProduct.getPathPicture());
						    bos.flush();
						    fos.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} /* Create file output stream */
						(ClientUI.clientController.getArrProducts()).add(anotherProduct);
					}
					break;
				}
			}
		}
	}

}

