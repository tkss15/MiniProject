package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class CEOController implements Initializable, IController {

	String userAreaName;

	@FXML
	private Text firstNameText;

	@FXML
	private Text lastNameText;

	@FXML
	private Text WelcomeMessageText;

	@FXML
	private Text IDText;

	@FXML
	private Text phoneNumberText;

	@FXML
	private Text emailText;

	@FXML
	private Button Logout;

	@FXML
	private Button monthlyReportsButton;

	@FXML
	private Button CloseButton;

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void close(ActionEvent event) {
		// This method will logout from the current user and finish the client-Side run
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		// Exit from the process
		System.exit(0);
	}

	/**
	 * method that triggers when the Logout button has been pressed
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void logout(ActionEvent event) {
		// logs out from the user and going back to the login screen
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	/**
	 * method that triggers when the Monthly reports button has been pressed
	 * 
	 * @author David
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void monthlyReports(ActionEvent event) {
		// This will show the monthly reports window, where the aCEO will be
		// able to watch different reports from any facility in any area
		ClientUI.sceneManager.ShowScene("../views/MonthlyReports.fxml", event);
	}

	/**
	 * retrieves the query data from the server to this method
	 * 
	 * @author David
	 * @param data that returns from the server
	 * 
	 */
	@Override
	public void updatedata(Object data) {
		// in this controller, I am not making any queries, so this method is unused

	}

	/**
	 * Initialize the fields of the view with data from the client's user.
	 * 
	 * @author David
	 * @param location  the location of the FXML file that loaded this controller
	 * @param resources the resources used to load the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// sets the user details
		firstNameText.setText(ClientUI.clientController.getUser().getFirstName());
		lastNameText.setText(ClientUI.clientController.getUser().getLastName());
		IDText.setText(ClientUI.clientController.getUser().getID());
		phoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		emailText.setText(ClientUI.clientController.getUser().getEmail());
		WelcomeMessageText
				.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));
	}

}
