package gui;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ClientInterfaceController {
	
	private ClientUI clientUI;
    @FXML
    private TextField ClientIDTextField;
    
    @FXML
    private TextArea clientTextAre;
    
	@FXML
    private Button CloseButton;

    @FXML
    private ImageView ConnectLogo;

    @FXML
    private ImageView Logo;

    @FXML
    private Button SearchButton;

    @FXML
    private Button UpdateButton;

    @FXML
    void closeWindow(ActionEvent event) {
    	System.exit(0);
    }
    @FXML
    void updateDetails(ActionEvent event) {
    	
    }
    @FXML
    void searchUser(ActionEvent event) {
    	String id = ClientIDTextField.getText();
    	if(id.trim().isEmpty())
		{
			System.out.println("You must enter an id number");	
		}
    	clientUI.clientController.accept(id);
    	
    	
    }

    public TextField getClientIDTextField() {
		return ClientIDTextField;
	}
	public void setClientIDTextField(TextField clientIDTextField) {
		ClientIDTextField = clientIDTextField;
	}

}
