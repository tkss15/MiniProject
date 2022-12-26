package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Entity.User;
import client.ChatClient;
import client.ClientUI;
import common.IController;
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
	private boolean logPhase = false;
	private boolean isLogged;
	private final Object lock = new Object();

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
    	//System.exit(0);
    }

    @FXML
    synchronized void actionLoggin(ActionEvent event) 
    {
    	if(userNameTextField.getText() == null || passwordTextField.getText() == null)
    		return;
    	String userName = userNameTextField.getText();
    	String password = passwordTextField.getText();
    	
    	RequestObjectClient request = new RequestObjectClient("#USER_LOGIN_DATA",String.format("table=users#condition=userName=%s&userPassword=%s#values=userName=username&userPassword=password", userName, password),"GET");    	
    	ClientUI.clientController.accept(request);
    	System.out.println("Hey" + Thread.currentThread().getName());
    	synchronized (lock) 
    	{
    	    while (!logPhase) {
    	        try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	}
		System.out.println("Hey 3" + Thread.currentThread().getName());
    	if(isLogged)
    	{
    		((Node) event.getSource()).getScene().getWindow().hide();
    		ClientUI.sceneManager.ShowScene("../views/Homepage.fxml");		
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
		/*Platform.runLater(() -> {*/
			System.out.println("ClientLoginPage");
			if(data instanceof ResponseObject)
			{
				ResponseObject serverResponse = (ResponseObject) data;
				switch(serverResponse.getRequest())
				{	
					case"#USER_LOGIN_DATA":
					{
						if(serverResponse.Responsedata.size() == 0)
						{
	//						Alert alert = new Alert(AlertType.INFORMATION, "Laptop LG 2 hours");
	//						alert.showAndWait();
							System.out.println("Laptop");
						}
						else
						{
							isLogged = true;
							Object[] values =(Object[]) serverResponse.Responsedata.get(0);
							
							String userName = (String)values[0];
							String userPassword = (String)values[1];
							ClientUI.clientController.setUser(new User(userName, userPassword)); 
							System.out.println("Hey 2" + Thread.currentThread().getName());
							
						}
				    	synchronized (lock) 
				    	{
				    		logPhase = true;
				    		lock.notifyAll();
				    	}
						break;
					}
					
				}
			}
		//});
	}

}
