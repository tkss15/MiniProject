package gui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class OrderPickupController implements Initializable, IController {
	
	private boolean isPickupValid = false;
	private Integer facilityCode;
	private String Status;
    @FXML
    private ImageView Logo;

    @FXML
    private Button CloseButton12;

    @FXML
    private Button CloseButton;

    @FXML
    private TextField textFieldCode;
    
    @FXML
    void closeWindow(ActionEvent event) 
    {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    void getOrderPickup(ActionEvent event) 
    {
    	isPickupValid = false;
    	
    	String orderCode = textFieldCode.getText();
    	Integer orderCodeValue = Integer.valueOf(orderCode);
    	// Only matches numbers
		if(!orderCode.matches("^(?:[1-9]|\\d\\d\\d*)$") || orderCode.equals(""))
		{
			Alert alert = new Alert(AlertType.ERROR, "Please insert a valid order code");
			alert.showAndWait();
			return;
		}
    	RequestObjectClient request = new RequestObjectClient("#GET_ORDER_PICKUP", String.format("%d#", orderCodeValue) ,"*");    
    	ClientUI.clientController.accept(request);
    	
    	
    	System.out.println(ClientUI.clientController.getEKFacility().getFacilityID());
    	if(!isPickupValid || ClientUI.clientController.getEKFacility().getFacilityID() != facilityCode)
    	{
			Alert alert = new Alert(AlertType.ERROR, "Error 404 Order not found.");
			alert.showAndWait();
			return;
    	}
    	
    	if(!Status.equals("SentToProvider"))
    	{
			Alert alert = new Alert(AlertType.ERROR, "Error Order was already taken.");
			alert.showAndWait();
			return;
    	}
    	
    	request = new RequestObjectClient("#UPDATE_ORDER_PICKUP",String.format("%d#",orderCodeValue),"PUT");    
    	ClientUI.clientController.accept(request);
  
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
      	alert.setTitle("Information");
      	alert.setHeaderText("You picked your order");
      	alert.setContentText("Your order is comming out in a few seconds..");

      	ButtonType buttonType = new ButtonType("Continue");
      	alert.getButtonTypes().setAll(buttonType);

      	Optional<ButtonType> result = alert.showAndWait();
      	if (result.get() == buttonType) 
      	{
      		closeWindow(event);
      	}
    	System.out.println("Your order picked up !");
    	
    }
    
	@Override
	public void updatedata(Object data) 
	{
		if(data instanceof ResponseObject)
		{
			ResponseObject serverResponse = (ResponseObject) data;
			switch(serverResponse.getRequest())
			{	
				case"#GET_ORDER_PICKUP":
				{
					System.out.println("Data 2?");
					if(!serverResponse.Responsedata.isEmpty())
					{
						Object[] values = (Object[])serverResponse.Responsedata.get(0);
						facilityCode = (Integer)values[0];
						Status = (String)values[1];
						isPickupValid = true;
						System.out.println("Hey this is the data"+ facilityCode + Status);
					}
					else
					{
						System.out.println("No data");
					}
				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ClientUI.clientController.setController(this);
		
	}

}

