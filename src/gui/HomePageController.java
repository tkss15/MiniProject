package gui;


import java.net.URL;
import java.util.ResourceBundle;

import Entity.Facility;
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
    void closeWindow(ActionEvent event) 
    {
    	ClientUI.clientController.UserDissconnected();
    	System.exit(0);
    }
    
    @FXML
    void openCatalogProduct(Event event) {
    	System.out.println("Closed");
    	
    	RequestObjectClient request = new RequestObjectClient("#FACILITY_LIST",String.format("table=facilities"),"GET");    	
    	ClientUI.clientController.accept(request);
    	
    	ClientUI.sceneManager.ShowScene("../views/ordersettings.fxml", event);
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
				case"#FACILITY_LIST":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						//	public Facility(int FacilityID, String FacilityLocation, String FacilityName, int FacilityThresholder)
						
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						String FacilityLocation = (String)values[1];
						String FacilityArea = (String)values[2];
						String FacilityName = (String)values[3];
						Integer FacilityThresholder = (Integer)values[4];
						boolean FacilityEK = (boolean)values[5];
						//ClientUI.clientController.(new Facility(FacilityID, FacilityLocation, FacilityName, FacilityThresholder));
						//System.out.println(arrFacility);
						ClientUI.clientController.arrFacility.add(new Facility(FacilityID,FacilityArea,FacilityLocation, FacilityName, FacilityThresholder,FacilityEK));
					}
					break;
				}
			
			}
		}
		
	}

}

