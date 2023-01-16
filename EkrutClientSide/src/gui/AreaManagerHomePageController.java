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

public class AreaManagerHomePageController implements Initializable, IController {

	@FXML
	private Text WelcomeMessageText;

	@FXML
	private Text firstNameText;

	@FXML
	private Text lastNameText;

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
	private Button approveCostumerButton;

	@FXML
	private Button CloseButton;

	@FXML
	private Button thresholdLevelButton;

	@FXML
	private Button refillOrderButton;

	/**
	 * method that triggers when the button Approve customer is being clicked
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void approveCostumerScreen(ActionEvent event) {
		// show the fxml for the area manager registration table, where he will choose
		// whom to approve or reject
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerRegistrationCenter.fxml", event);
	}

	/**
	 * method that triggers when the the button Refill order is being clicked
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void refillOrder(ActionEvent event) {
		// Opens the windows where the area manager will send executive orders
		ClientUI.sceneManager.ShowSceneNew("../views/RefillOrder.fxml", event);
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
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
	 * method that triggers when the Monthly reports button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void MonthlyReportsScreen(ActionEvent event) {
		// This will show the monthly reports window, where the area manager will be
		// able to watch different reports
		ClientUI.sceneManager.ShowSceneNew("../views/MonthlyReports.fxml", event);
	}

	/**
	 * method that triggers when the Threshold Level button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void ThresholdLevelScreen(ActionEvent event) {
		// This will show the Threshold level screen, where the Area Manager will update
		// the threshold Level to facilities
		ClientUI.sceneManager.ShowSceneNew("../views/ThresholdLevelSelect.fxml", event);
	}

	/**
	 * method that triggers when the Logout button has been pressed
	 * 
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
	 * Initialize the fields of the view with data from the client's user.
	 * 
	 * @param location  the location of the FXML file that loaded this controller
	 * @param resources the resources used to load the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// sets the user details
		ClientUI.clientController.setController(this);
		firstNameText.setText(ClientUI.clientController.getUser().getFirstName());
		lastNameText.setText(ClientUI.clientController.getUser().getLastName());
		IDText.setText(ClientUI.clientController.getUser().getID());
		phoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		emailText.setText(ClientUI.clientController.getUser().getEmail());
		WelcomeMessageText
				.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));
	}

	/**
	 * retrieves the query data from the server to this method
	 * 
	 * @param data that returns from the server
	 * 
	 */
	@Override
	public void updatedata(Object data) {
		// in this controller, I am not making any queries, so this method is unused
	}

}
