package gui;

import java.net.URL;
import java.util.ResourceBundle;

import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ClientLoginPage implements Initializable, IController
{
	private boolean isLogged;
	private boolean alreadyLogged = false;
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
    private ImageView FastLogin;
    @FXML
    private Button LoginApp;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    void ExitWindow(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
    void actionLoggin(ActionEvent event) 
    {
    	System.out.println("A "+ userNameTextField.getText());
    	System.out.println("B "+ passwordTextField.getText());
    	if(userNameTextField.getText().equals("")|| passwordTextField.getText().equals("") )
    	{
    		errorLabel.setText(null);
    		errorLabel.setVisible(true);
    		return;
    	}
    	String userName = userNameTextField.getText();
    	String password = passwordTextField.getText();
    	
    	
    	RequestObjectClient request = new RequestObjectClient("#USER_LOGIN_DATA",String.format("table=users#condition=userName=%s&userPassword=%s", userName, password),"GET");    	
    	ClientUI.clientController.accept(request);

    	if(isLogged && !alreadyLogged)
    	{
    		request = new RequestObjectClient("#USER_UPDATELOGIN",String.format("table=users#condition=userName=%s#values=userOnline=\"Online\"", userName),"PUT");    	
        	ClientUI.clientController.accept(request);

    		ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);		
    	}
    	else
    	{
    		errorLabel.setText((alreadyLogged) ? "Error User already Logged-In" : "Error authentication failed, Try again.");
    		errorLabel.setVisible(true);
    	}

    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		errorLabel.setVisible(false);
		if(ClientUI.clientController.getEKFacility() != null && ClientUI.clientController.getEKFacility().isFacilityEK())
		{
			FastLogin.setVisible(true);
			LoginApp.setVisible(false);
		}
		else
		{
			FastLogin.setVisible(false);
			LoginApp.setVisible(true);
		}
		exitbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			System.out.println("Hello");
			event.consume();
		});
		
    }

	@Override
	public void updatedata(Object data) {
			System.out.println("ClientLoginPage");
			if(data instanceof ResponseObject)
			{
				ResponseObject serverResponse = (ResponseObject) data;
				
				switch(serverResponse.getRequest())
				{	
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
							String OnlineUser = (String)values[7];
							System.out.println(OnlineUser);
							if(OnlineUser.equals("Online"))
							{
								alreadyLogged = true;
								return;
							}
							
							alreadyLogged = false;
							//	public User(String firstName, String lastName, String phone, String email, String ID, String UserName,
							ClientUI.clientController.setUser(new User(firstName, LastName, Telephone, Email, ID, userName, userPassword)); 
							ClientUI.clientController.getUser().setOnlineStatus("Online");
							System.out.println("Hey 2" + Thread.currentThread().getName());
							
						}
						break;
					}
					
				}
			}
	}

}
