package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Entity.Facility;
import Entity.User;
import client.ChatClient;
import client.ClientUI;
import common.IController;
import common.MyFile;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ClientLoginPage implements Initializable, IController
{
	private boolean isLogged;

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
    void SimpleRequest(ActionEvent event)
    {
    	RequestObjectClient request = new RequestObjectClient("#SIMPLE_REQUEST",String.format("table=products"),"GET");    	
    	ClientUI.clientController.accept(request);
    }
    @FXML
    void ExitWindow(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
    void actionLoggin(ActionEvent event) 
    {
    	if(userNameTextField.getText() == null || passwordTextField.getText() == null)
    		return;
    	String userName = userNameTextField.getText();
    	String password = passwordTextField.getText();
    	
//    	RequestObjectClient request = new RequestObjectClient("#USER_LOGIN_DATA",String.format("table=users#condition=userName=%s&userPassword=%s#values=userName=username&userPassword=password", userName, password),"GET");    	
    	RequestObjectClient request = new RequestObjectClient("#USER_LOGIN_DATA",String.format("table=users#condition=userName=%s&userPassword=%s", userName, password),"GET");    	

    	ClientUI.clientController.accept(request);

    	if(isLogged)
    	{
        	request = new RequestObjectClient("#USER_UPDATELOGIN",String.format("table=users#condition=userName=%s#values=userOnline=\"Online\"", userName),"PUT");    	
        	ClientUI.clientController.accept(request);
        	//UPDATE `ekrutdatabase`.`users` SET `userOnline` = 'Offline' WHERE (`userName` = 'tkss15');
        	//UPDATE users SET userOnline = "Online" WHERE userName = tkss15

    		ClientUI.sceneManager.ShowScene("../views/Homepage.fxml", event);		
    	}
    	else
    	{
//    		System.out.println("Login failed");
//			Alert alert = new Alert(AlertType.ERROR, "Laptop LG 2 hours");
//			alert.showAndWait();
    	}

    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		exitbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			System.out.println("Hello");
			event.consume();
		});
		
    }


	@Override
	public void updatedata(Object data) {
		// TODO Auto-generated method stub
		//Platform.runLater(() -> {
			System.out.println("ClientLoginPage");
			if(data instanceof ResponseObject)
			{
				ResponseObject serverResponse = (ResponseObject) data;
				
				switch(serverResponse.getRequest())
				{	
					case"#SIMPLE_REQUEST":
					{
						System.out.println("HELLO");
						Object[] values =(Object[]) serverResponse.Responsedata.get(0);//Row 1 
						Integer ProductCode = (Integer) values[0];
						String ProductName = (String) values[1];
						Double ProductPrice = (Double) values[2];
						String ProductDesc = (String) values[3];
						String ProductPicture = (String) values[4];

						System.out.println(ProductCode + ProductName + ProductPrice + ProductDesc + ProductPicture);
						break;
					}

					case"#USER_LOGIN_DATA":
					{
						if(serverResponse.Responsedata.size() != 0)
						{
							isLogged = true;
							Object[] values =(Object[]) serverResponse.Responsedata.get(0);//Row 1 
							
							String firstName = (String)values[0];
							String LastName = (String)values[1];
							String Telephone = (String)values[2];
							String Email = (String)values[3];
							String ID = (String)values[4];
							String userName = (String)values[5];
							String userPassword = (String)values[6];
							
							//	public User(String firstName, String lastName, String phone, String email, String ID, String UserName,
							ClientUI.clientController.setUser(new User(firstName, LastName, Telephone, Email, ID, userName, userPassword)); 
							ClientUI.clientController.getUser().setOnlineStatus("Online");
							System.out.println("Hey 2" + Thread.currentThread().getName());
							
						}
						break;
					}
					
				}
			}
		//});
	}

}
