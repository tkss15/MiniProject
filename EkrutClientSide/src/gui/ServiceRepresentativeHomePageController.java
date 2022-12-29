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

public class ServiceRepresentativeHomePageController implements Initializable,IController{

    @FXML
    private Button logoutButton;

    @FXML
    private Button CloseButton;

    @FXML
    private Button RegistrationFormButton;

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
    	((Node) event.getSource()).getScene().getWindow().hide();
    	ClientUI.sceneManager.ShowScene("../views/LoginClientInterface.fxml");
    }

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("ServiceRepresentativeInterface");
	}

}
