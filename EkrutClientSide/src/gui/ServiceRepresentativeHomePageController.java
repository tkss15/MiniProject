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
    /**
     * redirects the user to the registration form interface via the scene manager class.
     * @param event ActionEvent that triggered this method call
     */
    void RegistrationForm(ActionEvent event) {
    	ClientUI.sceneManager.ShowSceneNew("../views/RegistrationFormInterface.fxml",event);
    }

    @FXML
    /**
     * closes the window and logs out from the account.
     * @param event ActionEvent that triggered this method call
     */
    void close(ActionEvent event) {
    	logout(event);
    	System.exit(0);
    }

    /**
	 * method that triggers when the Logout button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void logout(ActionEvent event) {
		// logs out from the user and going back to the login screen
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	@Override
	/**
	 * not used in this particular controller.
	 */
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * Initializes the logged in user's personal data in the text fields on the screen.
		 */
		firstNameText.setText(ClientUI.clientController.getUser().getFirstName());
		lastNameText.setText(ClientUI.clientController.getUser().getLastName());
		idText.setText(ClientUI.clientController.getUser().getID());
		phoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		emailText.setText(ClientUI.clientController.getUser().getEmail());
		welcomeMessageText.setText(String.format("Welcome Back %s",ClientUI.clientController.getUser().getFirstName()));
	}

}
