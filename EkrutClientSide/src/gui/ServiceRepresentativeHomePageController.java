package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class ServiceRepresentativeHomePageController implements Initializable,IController{

    @FXML
    private Button logoutButton;

    @FXML
    private Button CloseButton;

    @FXML
    private Button RegistrationFormButton;
    
    @FXML
    private Text welcomeMessageText;

    @FXML
    private Text firstNameText;

    @FXML
    private Text lastNameText;

    @FXML
    private Text idText;

    @FXML
    private Text phoneNumberText;

    @FXML
    private Text emailText;


    @FXML
    void RegistrationForm(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	ClientUI.sceneManager.ShowScene("../views/RegistrationFormInterface.fxml");
    }

    @FXML
    void close(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void logout(ActionEvent event) {
    	try {
    		
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
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}
    	ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
    	//System.exit(0);
    }

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("ServiceRepresentativeInterface");
		firstNameText.setText(ClientUI.clientController.getUser().getFirstName());
		lastNameText.setText(ClientUI.clientController.getUser().getLastName());
		idText.setText(ClientUI.clientController.getUser().getID());
		phoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		emailText.setText(ClientUI.clientController.getUser().getEmail());
		welcomeMessageText.setText(String.format("Welcome Back %s",ClientUI.clientController.getUser().getFirstName()));
	}

}
