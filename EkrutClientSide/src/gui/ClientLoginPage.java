package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ClientLoginPage implements Initializable
{

    @FXML
    private VBox vboxlogo;

    @FXML
    private ImageView exitbutton;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passwordTextField;
    
    @FXML
    private Button LoginButton;

    @FXML
    private Button LoginApp;
    
    @FXML
    void ExitWindow(MouseEvent event) {
    	System.exit(0);
    }
    
    

    @FXML
    void actionLoggin(ActionEvent event) 
    {
    	if(userNameTextField.getText() == null || passwordTextField.getText() == null)
    		return;
    	
    	ArrayList<String> dataRequest = new ArrayList<String>();
    	dataRequest.add("USER_LOGIN");
    	dataRequest.add(userNameTextField.getText());
    	dataRequest.add(passwordTextField.getText());
    	
    	String finalString = dataRequest.toString();
    	finalString = finalString.substring(1, finalString.length()-1);
    	ClientUI.clientController.accept(finalString);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		exitbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			System.out.println("Hello");
			event.consume();
		});
    }

}
