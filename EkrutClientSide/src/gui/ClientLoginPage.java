package gui;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Entity.Employee;
import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ClientLoginPage implements Initializable, IController
{
	private boolean isLogged;
	private boolean alreadyLogged = false;
	private boolean isEmployee = false;
	private String role;

	private User user;
    @FXML
    private VBox vboxlogo;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passwordTextField;
    
    @FXML
    private Button LoginButton;

    @FXML
    private Button LoginApp;
    
    @FXML
    private Label errorLabel;
    /**
     * fast login window, the user will be able to login with ekrut touch if he is subscribed
     * @param
     * */
    @FXML
    void ShowFastLogin(ActionEvent event) {
    	ClientUI.sceneManager.ShowSceneNew("../views/FastLoginInterface.fxml", event);	
    }
    /**
     * triggers when the "X" is being pressed
     * exists from the system
     * @param event*/
    @FXML
    void ExitWindow(ActionEvent event) 
    {
    	RequestObjectClient request = new RequestObjectClient("#USER_QUIT","","*");    	
    	ClientUI.clientController.accept(request);
    	System.exit(0);
    }
    /***
     * when the user presses login, the software will check wether he has a user in the databse
     * all fields must be filled in order for this button to activate.
     * messages of error will be displayed if one of the fields are not correct
     * also if the user is an employee he will have the option to choose to that interface he wants to enter
     * @param
     * */
    @FXML
    void actionLoggin(ActionEvent event) 
    {
    	if(userNameTextField.getText() == null || userNameTextField.getText().trim().isEmpty())
    	{
    		errorLabel.setText("Error missing username or password");
    		errorLabel.setVisible(true);
    		return;
    	}
    	if(passwordTextField.getText() == null || passwordTextField.getText().trim().isEmpty())
    	{
    		errorLabel.setText("Error missing username or password");
    		errorLabel.setVisible(true);
    		return;
    	}
    	String userName = userNameTextField.getText();
    	String password = passwordTextField.getText();


    	RequestObjectClient request = new RequestObjectClient("#USER_LOGIN_DATA",String.format("%s#%s#", userName, password),"GET");    	
    	ClientUI.clientController.accept(request);
    	
    	if(isLogged && !alreadyLogged)
    	{
    		request = new RequestObjectClient("#USER_UPDATELOGIN",String.format("%s#", userName),"PUT");    	
    		ClientUI.clientController.accept(request);
    		
    		request = new RequestObjectClient("#USER_IS_EMPLOYEE",String.format("%s#", userName),"GET");
    		ClientUI.clientController.accept(request);
    		if(isEmployee)
    		{
        		Alert alert = new Alert(AlertType.CONFIRMATION);
        		alert.setTitle("Login");
        		alert.setHeaderText("Choose Your login destention");
        		alert.setContentText("Choose your option.");

        		ButtonType clientHompage = new ButtonType("Client Homepage");
        		ButtonType employeeHomepage = new ButtonType("Employee Homepage");

        		alert.getButtonTypes().setAll(clientHompage, employeeHomepage);
    			Optional<ButtonType> result = alert.showAndWait();
    			
        		if (result.get() == clientHompage)
        		{
        			ClientUI.sceneManager.ShowSceneNew("../views/Homepage.fxml", event);
        			return;
        		} 
        		else if (result.get() == employeeHomepage) 
        		{
        			String open= new String(); 
        			open=String.format("../views/%sInterface.fxml",role);
        			ClientUI.sceneManager.ShowSceneNew(open, event);
        			return;
        		}
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
    /**
     * this method is being called when this controller is activated
     * @param - location
     * @param - resources*/
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		isEmployee = false;
		user = new User(null,null);
    	errorLabel.setVisible(false);
		LoginApp.setVisible(true);
		
    }
	/**
	 * the data that is being returned from the queries
	 *@param - data
	 */
	@Override
	public void updatedata(Object data) {
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
							ClientUI.clientController.setUser(user); 
							
						}
						break;
					}
					case "#USER_IS_EMPLOYEE":
					{
						if(serverResponse.Responsedata.size() != 0)
						{
							Object[] values =(Object[]) serverResponse.Responsedata.get(0);//Row 1 
							role=(String)values[0];
							isEmployee=true;
							Employee employee = new Employee(ClientUI.clientController.getUser());
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
