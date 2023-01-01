package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Employee;
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
	private boolean isEmployee = false; /////////
	private String role;

	private User user;
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
    		
    		request = new RequestObjectClient("#USER_IS_EMPLOYEE",String.format("table=Employees#condition=userName=%s", userName),"GET");
    		ClientUI.clientController.accept(request);
    		if(isEmployee)
    		{
    			String open= new String(); 
    			open=String.format("../views/%sInterface.fxml",role);
    			ClientUI.sceneManager.ShowSceneNew(open, event);
    			return;
    		}

    		ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);	
    		
    		
    		// Controller -> {orders table} -> Server | ....... | Server -> {data} -> Controller 
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
		isEmployee = false;
		user = new User(null,null);
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
							String AreaUser = (String)values[8];

							
							if(OnlineUser.equals("Online"))
							{
								alreadyLogged = true;
								return;
							}
							
							user.setFirstName(firstName);
							user.setLastName(LastName);
							user.setPhone(Telephone);

							user.setEmail(Email);

							user.setID(ID);
							user.setUserName(userName);
							user.setPassword(userPassword);
							user.setOnlineStatus("Online");
							user.setArea(AreaUser);
							alreadyLogged = false;
							//	public User(String firstName, String lastName, String phone, String email, String ID, String UserName,
							ClientUI.clientController.setUser(user); 
							System.out.println("Hey 2 " + ClientUI.clientController.getUser());
							
						}
						break;
					}
					case "#USER_IS_EMPLOYEE":
					{
						System.out.println(serverResponse.Responsedata.size());
						if(serverResponse.Responsedata.size() != 0)
						{
							Object[] values =(Object[]) serverResponse.Responsedata.get(0);//Row 1 
							role=(String)values[0];
//							String userName=(String)values[1];
							String branch=(String)values[2];
							isEmployee=true;
							System.out.println("Employee");
							Employee employee = new Employee(ClientUI.clientController.getUser(),branch);
							ClientUI.clientController.setUser(employee); 
						}
						else
						{
							isEmployee = false;
						}
					}
					break;
					
				}
			}
	}

}
