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
	private String ID;
	private String Location;
	boolean exists = false;
	private ArrayList<Facility> Facilities;
	private ArrayList<String> arrayId;
	private ArrayList<String> arrayLocation;
	private ArrayList<String> arrayName;

	ObservableList<String> IDList;
	ObservableList<String> LocationList;
	ObservableList<String> NameList;

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

	@FXML
	private Button ThresholdLevelButton;

	@FXML
	private Button ApproveButton;

	@FXML
	void approve(ActionEvent event) {

	}

	@FXML
	void close(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("table=users#condition=userName=%s#values=userOnline=\"Offline\"",
							ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
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

	@FXML
	void monthlyReports(ActionEvent event) {
		
	}

	@FXML
	void thresholdLevel(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/ThresholdLevelSelect.fxml", event);
	}

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub

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
