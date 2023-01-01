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
	void approveCostumerScreen(ActionEvent event) {

	}

	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void MonthlyReportsScreen(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.sceneManager.ShowScene("../views/MonthlyReports.fxml");
	}

	@FXML
	void ThresholdLevelScreen(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.sceneManager.ShowScene("../views/ThresholdLevelSelect.fxml");
	}

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub

	}

	@FXML
	void logout(ActionEvent event) {
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
    	ClientUI.sceneManager.ShowSceneNew("../views/LoginClientInterface.fxml", event);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		firstNameText.setText(ClientUI.clientController.getUser().getFirstName());
		lastNameText.setText(ClientUI.clientController.getUser().getLastName());
		IDText.setText(ClientUI.clientController.getUser().getID());
		phoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		emailText.setText(ClientUI.clientController.getUser().getEmail());
		WelcomeMessageText.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));
	}

}
