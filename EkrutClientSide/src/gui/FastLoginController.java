package gui;

import java.net.URL;
import java.util.ResourceBundle;

import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class FastLoginController implements Initializable, IController {

	boolean isSubscriber = false;
	boolean isLogin = false;
	@FXML
	private TextField textSubscriberID;

	@FXML
	private VBox PhoneVBox;

	@FXML
	private TextField phoneAuth;

	@FXML
	private Label errorLabel;

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void closeWindow(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	/**
	 * This method is used to handle the action of the login button. It checks that
	 * the subscriber ID and phone auth code that the subscriber recieved on his
	 * phone are valid. If the input is valid, it creates a request object and sends
	 * it to the client controller. If the login is successful, it changes the scene
	 * to the homepage of the subscriber.
	 * 
	 * @param event - The event that triggered this method - clicking the login
	 *              button
	 */
	@FXML
	void LoginAction(ActionEvent event) {
		// Check if subscriber ID is empty or null
		if (textSubscriberID.getText().trim().isEmpty() || textSubscriberID.getText() == null) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error Subscriber cant be empty");
			return;
		}

		// Check if subscriber ID contains only numbers
		if (textSubscriberID.getText() != null && !textSubscriberID.getText().matches("[0-9]+")) {
			errorLabel.setVisible(true);
			errorLabel.setText("Error Subscriber must contain only numbers");
			return;
		}

		RequestObjectClient request;

		// Check if user is a subscriber and phone auth code is not empty or null
		if (isSubscriber) {
			if (phoneAuth.getText().trim().isEmpty() || phoneAuth.getText() == null) {
				errorLabel.setVisible(true);
				errorLabel.setText("Error Code cant be empty");
				return;
			}
			
			// Check if phone auth code contains only numbers
			if (phoneAuth.getText() != null && !phoneAuth.getText().matches("[0-9]+")) {
				errorLabel.setVisible(true);
				errorLabel.setText("Error Code must contain only numbers");
				return;
			}
			request = new RequestObjectClient("#GET_USER_AUTHCODE", String.format("%s#", phoneAuth.getText()), "*");
		} else {
			request = new RequestObjectClient("#GET_USER_SUBSCRIBER_NUBMER",
					String.format("%s#", textSubscriberID.getText()), "*");
		}
		ClientUI.clientController.accept(request);
		// Check if login is successful and change the scene to the homepage
		if (isLogin) {
			ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);
			ClientUI.sceneManager.SceneBack(event, "../views/LoginClientInterface.fxml");
		}
	}

	/**
	 * saving all the data which is returned from the DB and relevant for the
	 * current controller.
	 */
	@Override
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;

			switch (serverResponse.getRequest()) {
			case "#GET_USER_SUBSCRIBER_NUBMER": {
				if (serverResponse.Responsedata.size() != 0) {
					isSubscriber = (boolean) serverResponse.Responsedata.get(0);
					Platform.runLater(() -> {
						if (isSubscriber) {
							textSubscriberID.setDisable(true);
							PhoneVBox.setVisible(true);
						} else {
							errorLabel.setVisible(true);
							errorLabel.setText("Subscriber number not valid");
						}
					});
				}
				break;
			}
			case "#GET_USER_AUTHCODE": {
				if (serverResponse.Responsedata.size() != 0) {
					isLogin = true;
					Object[] values = (Object[]) serverResponse.Responsedata.get(0);// Row 1

					String firstName = (String) values[0];
					String LastName = (String) values[1];
					String Telephone = (String) values[2];
					String Email = (String) values[3];
					String ID = (String) values[4];
					String userName = (String) values[5];
					String userPassword = (String) values[6];
					String AreaUser = (String) values[8];

					ClientUI.clientController.getUser().setFirstName(firstName);
					ClientUI.clientController.getUser().setLastName(LastName);
					ClientUI.clientController.getUser().setPhone(Telephone);

					ClientUI.clientController.getUser().setEmail(Email);

					ClientUI.clientController.getUser().setID(ID);
					ClientUI.clientController.getUser().setUserName(userName);
					ClientUI.clientController.getUser().setPassword(userPassword);
					ClientUI.clientController.getUser().setOnlineStatus("Online");
					ClientUI.clientController.getUser().setArea(AreaUser);
				}
				break;
			}
			}
		}

	}

	/**
	 * initialises the controller as it is loaded.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		PhoneVBox.setVisible(false);
		errorLabel.setVisible(false);
	}

}
