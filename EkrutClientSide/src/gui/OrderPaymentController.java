package gui;

import common.IController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class OrderPaymentController implements IController {

    @FXML
    private Button CloseButton;

    @FXML
    private ImageView Logo;

    @FXML
    private ImageView Logo22;

    @FXML
    private Button CloseButton3;

    @FXML
    private Button CloseButton2;

    @FXML
    private ImageView Logo221;

    @FXML
    private Button CloseButton1;

    @FXML
    private TextField ClientIDTextField;

    @FXML
    private TextField ClientIDTextField1;

    @FXML
    private TextField ClientIDTextField2;

    @FXML
    private TextField ClientIDTextField3;

    @FXML
    private TextField ClientIDTextField4;

    @FXML
    private TextField ClientIDTextField41;

    @FXML
    private Button CloseButton11;

    @FXML
    private ImageView Logo1;

    @FXML
    private ImageView Logo2;

    @FXML
    private ImageView Logo21;

    @FXML
    private ImageView Logo211;

    @FXML
    private ImageView Logo212;

    @FXML
    void closeWindow(ActionEvent event) {
    	System.out.println("Closed");
    }

	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		
	}

}
