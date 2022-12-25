package gui;

import client.ClientUI;
import common.RequestObjectClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ClientLoginInterface {
	private RequestObjectClient request;
	@FXML
	private Button UpdateButton;

	@FXML
	private ImageView ConnectLogo;
	@FXML
	private TextField textFiledIP;
	@FXML
	private ImageView Logo;

	@FXML
	private Button CloseButton;


	@FXML
	void ConnectToDB(ActionEvent event) throws Exception 
	{
		String ipaddress = textFiledIP.getText();

		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.ConnectToServer(ipaddress);
		
		request = new RequestObjectClient("table=users#condition=userName=tkss15&userPassword=123456","GET");    	
		ClientUI.clientController.accept(request);
		
		ClientUI.sceneManager.ShowScene("../views/Homepage.fxml");
		
	}


	@FXML
	void exitFromLogin(ActionEvent event) {
		System.exit(0);
	}

}
