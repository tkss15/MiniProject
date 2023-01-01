package gui;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.Product;
import client.ClientConsole;
import client.ClientUI;
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
import javafx.scene.text.Text;

public class HomePageController implements Initializable, IController
{
	@FXML
	private Text textUserlogin;

	@FXML
    private Text textFirstName;

    @FXML
    private Text textLastName;

    @FXML
    private Text textID;

    @FXML
    private Text textTelephone;

    @FXML
    private Text textEmail;
    
    @FXML
    private Button Logout;

    @FXML
    private Button ManageOrders;

    @FXML
    private Button CloseButton;

    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2, BtnCreateOrder;

    @FXML 
    void Logout(ActionEvent event)
    {
		if(ClientUI.clientController.getUser().getOnlineStatus() == null)
		{
			System.out.println("Not updated");
		}
		if(ClientUI.clientController.getUser().getOnlineStatus().equals("Online"))
		{
	    	RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",String.format("table=users#condition=userName=%s#values=userOnline=\"Offline\"", ClientUI.clientController.getUser().getUserName()),"PUT");    
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
        	String sql = "SELECT products.*, productsinfacility.ProductAmount FROM products LEFT JOIN productsinfacility ON products.ProductCode = productsinfacility.ProductCode WHERE productsinfacility.FacilityID = " + ClientUI.clientController.getEKFacility().getFacilityID() + " ORDER BY products.ProductCode";
        	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",sql,"*");  
        	ClientUI.clientController.accept(request);
        	
    		ClientUI.sceneManager.ShowScene("../views/CatalogViewer.fxml", event);
    	}
    	else {
    		ClientUI.sceneManager.ShowScene("../views/ordersettings.fxml", event);
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textUserlogin.setText(ClientUI.clientController.getUser().getFirstName());
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());
		
		BtnCreateOrder.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			openCatalogProduct(event);
		});
	}

	@Override
	public void updatedata(Object data) {
		System.out.println("HomePageController");
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
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

