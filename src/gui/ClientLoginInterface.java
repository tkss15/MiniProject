package gui;

import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ClientLoginInterface implements IController {
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
		
//		request = new RequestObjectClient("USER_RETURN_DATA","table=users#condition=userName=tkss15","GET");    	
//		ClientUI.clientController.accept(request);
		
		ClientUI.sceneManager.ShowScene("../views/LoginClientInterface.fxml");
		
	}


	@FXML
	void exitFromLogin(ActionEvent event) {
		System.exit(0);
	}


	@Override
	public void updatedata(Object data) {
		System.out.println("ClientLoginInterface");
	}

}
