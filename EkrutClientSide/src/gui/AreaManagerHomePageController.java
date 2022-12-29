package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ClientUI;
import common.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class AreaManagerHomePageController implements Initializable, IController{

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
    private Text locationText;

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
    private Text WelcomeBackMessage;

    @FXML
    void approveCostumerScreen(ActionEvent event) {
    	
    }

    @FXML
    void close(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void showMonthlyReportsScreen(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	ClientUI.sceneManager.ShowScene("../views/MonthlyReports.fxml");
    }

    @FXML
    void showThresholdLevelScreen(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	ClientUI.sceneManager.ShowScene("../views/ThresholdLevelSelect.fxml");
    }

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		
	}
	

    @FXML
    void logout(ActionEvent event) {
    	((Node) event.getSource()).getScene().getWindow().hide();
    	ClientUI.sceneManager.ShowScene("../views/LoginClientInterface.fxml");
    }
    	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ClientUI.clientController.setController(this);
		firstNameText.setText(ClientUI.clientController.getUser().getFirstName());
		lastNameText.setText(ClientUI.clientController.getUser().getLastName());
		IDText.setText(ClientUI.clientController.getUser().getID());
		phoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		emailText.setText(ClientUI.clientController.getUser().getEmail());
	}


}
