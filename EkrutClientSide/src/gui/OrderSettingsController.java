package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;

public class OrderSettingsController implements Initializable {

	@FXML
	private ComboBox<?> ComboboxFacility;
    @FXML
    private Button CloseButton;

    @FXML
    private ImageView Logo;

    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2;

    @FXML
    private ImageView Logo221;

    @FXML
    private Button CloseButton11;

    @FXML
    private ImageView Logo1;

    @FXML
    private ImageView Logo11;

    @FXML
    private Button CloseButton1;

    @FXML
    void closeWindow(ActionEvent event) {
    	System.out.println("Closed");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
