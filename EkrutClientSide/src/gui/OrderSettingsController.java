package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class OrderSettingsController implements Initializable, IController {

	ToggleGroup tg = new ToggleGroup();
	ArrayList<Facility> arrFacility = new ArrayList<>();
	ObservableList<Facility> list;
	
	@FXML
	private ComboBox<Facility> ComboboxFacility;
    @FXML
    private Button CloseButton;
    @FXML
    private ImageView Logo;

  
    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2;

    @FXML
    private ImageView Logo221;
    
    
    @FXML
    private Button BackButton;

    @FXML
    private ImageView Logo1;

    @FXML
    private ImageView Logo11;
    
    @FXML
    private Button CloseButton1;

    @FXML
    private RadioButton radioDelivery;

    @FXML
    private RadioButton radioPickup;
    
    @FXML
    void closeWindow(ActionEvent event) {
    	System.out.println("Closed");
    }
    
    @FXML
    void actionCreateOrder(ActionEvent event)
    {
    	if(ComboboxFacility.getValue() != null)
    	{
        	ClientUI.clientController.getClientOrder().setFacilityType("EK");
        	ClientUI.clientController.getClientOrder().setOrderFacility(ComboboxFacility.getValue());
        	ClientUI.clientController.getClientOrder().setOrderType(((RadioButton)tg.getSelectedToggle()).getText());
        	ClientUI.sceneManager.ShowScene("../views/CatalogViewer.fxml", event);
    	}
    }
    @FXML
    void BackAction(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		radioDelivery.setToggleGroup(tg);
		radioPickup.setToggleGroup(tg);

		list = FXCollections.observableArrayList(ClientUI.clientController.arrFacility);
		ComboboxFacility.setItems(list);
	}

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		System.out.println("OrderSettingsController");
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
				case"#FACILITY_LIST":
				{
					for(int i = 0; i < serverResponse.Responsedata.size(); i++)
					{
						Object[] values =(Object[]) serverResponse.Responsedata.get(i);
						Integer FacilityID = (Integer)values[0];
						String FacilityLocation = (String)values[1];
						String FacilityName = (String)values[2];
						Integer FacilityThresholder = (Integer)values[3];
						arrFacility.add(new Facility(FacilityID, FacilityLocation, FacilityName, FacilityThresholder));
						System.out.println(arrFacility);
					}
					break;
				}
				
			}
		}
	}

}
