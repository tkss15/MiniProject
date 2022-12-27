package gui;


import java.net.URL;
import java.util.ResourceBundle;

import client.ClientConsole;
import client.ClientUI;
import common.IController;
import common.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class HomePageController implements Initializable, IController
{

	@FXML
    private Text textFirstName;

    @FXML
    private Text textLastName;

    @FXML
    private Text textID;

    @FXML
    private Text textTelephone;

    @FXML
    private Text textEmail;
    
    @FXML
    private Button Logout;

    @FXML
    private Button ManageOrders;

    @FXML
    private Button CloseButton;

    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2, BtnCreateOrder;

    @FXML
    void closeWindow(ActionEvent event) {
    	System.out.println("Closed");
    }
    
    @FXML
    void openCatalogProduct(ActionEvent event) {
    	System.out.println("Closed");
    	//((Node) event.getSource()).getScene().getWindow().hide();
    	ClientUI.sceneManager.ShowScene("../views/ordersettings.fxml");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());
	}

	@Override
	public void updatedata(Object data) {
		System.out.println("HomePageController");
		
	}

}

