package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entity.Facility;
import client.ClientUI;
import common.CountdownOrder;
import common.IController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private Label errorLabel;
    
    @FXML
    void closeWindow(ActionEvent event) {
    	System.out.println("Closed");
    }
    
    @FXML
    void actionCreateOrder(ActionEvent event)
    {
    	if(ComboboxFacility.getValue() == null)
    	{
    		errorLabel.setVisible(true);
    		errorLabel.setText("Error: You have to pick a Facility to order from.");
    		return;
    	}
    	if(tg.getSelectedToggle() == null)
    	{
    		errorLabel.setVisible(true);
    		errorLabel.setText("Error: You have to pick either Delivery or Pickup");
    		return;
    	}
    	if(ComboboxFacility.getValue() != null)
    	{
        	
        	ClientUI.clientController.getClientOrder().setFacilityType("OL");
        	ClientUI.clientController.getClientOrder().setOrderFacility(ComboboxFacility.getValue());
        	ClientUI.clientController.getClientOrder().setOrderType(((RadioButton)tg.getSelectedToggle()).getText());
        	ClientUI.clientController.setTaskCountdown(new CountdownOrder());
        	ClientUI.sceneManager.ShowSceneNew("../views/CatalogViewer.fxml", event);
    	}
    }
    @FXML
    void ShowPrevPage(ActionEvent event)
    {
    	ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
   		errorLabel.setVisible(false);
		radioDelivery.setToggleGroup(tg);
		radioPickup.setToggleGroup(tg);
		for(Facility f : ClientUI.clientController.getArrFacility())
		{
			System.out.println(f);
		}
		//System.out.println(ClientUI.clientController.getUser().getArea());
		list = FXCollections.observableArrayList(ClientUI.clientController.getArrFacility().stream()
				.filter(fac -> (fac.getFacilityArea().equals(ClientUI.clientController.getUser().getArea())) )
				.collect(Collectors.toList()));
		//list = FXCollections.observableArrayList(ClientUI.clientController.arrFacility);
		ComboboxFacility.setItems(list);
	}

	@Override
	public void updatedata(Object data) {
	}

}
