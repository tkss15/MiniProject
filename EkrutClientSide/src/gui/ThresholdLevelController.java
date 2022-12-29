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
import javafx.scene.control.SplitMenuButton;

public class ThresholdLevelController implements Initializable, IController {

	@FXML
	private Button backButton;

	@FXML
	private Button CloseButton;

	@FXML
	private SplitMenuButton selectFacilityButton;

	@FXML
	private SplitMenuButton SelectThresoldButton;

	@FXML
	private Button approveButton;

	@FXML
	void approve(ActionEvent event) {

	}

	@FXML
	void back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.sceneManager.ShowScene("../views/AreaManager.fxml");
	}

	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void selectFacility(ActionEvent event) {

	}

	@FXML
	void selectThreshold(ActionEvent event) {

	}

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
