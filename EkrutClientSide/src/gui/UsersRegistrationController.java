package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UsersRegistrationController implements Initializable, IController {

	/**
	 * this class extends the User class. inner class for representation of the rows
	 * of the table. ImportedUser saves information about users which want to
	 * register to our system. ImportedUser saves all of user's attributes (as it
	 * extends the User class) but also it saves the user credit card, it saves a
	 * boolean which shows if the user was sent to manager for acception, and it
	 * also saves the user type registration request - which can be one of the
	 * following ('Registered','Subscriber','Registered To Subscriber').
	 * 
	 */
	public class ImportedUser extends User {

		private String userTypeRequest;
		private boolean isSentToManager;
		private String creditCard;

		/**
		 * constructor of the ImportedUserd class.
		 * 
		 * @param user
		 * @param area
		 * @param isSentToManager
		 * @param registrationsType
		 * @param status
		 * @param cardNumber
		 */
		public ImportedUser(User user, String area, String userTypeRequest, boolean isSentToManager) {
			super(user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail(), user.getID(),
					user.getUserName(), user.getPassword(), user.getArea());

			this.userTypeRequest = userTypeRequest;
			this.isSentToManager = isSentToManager;
		}

		/* standard getters and setters */
		public String getRoleTypeRequest() {
			return userTypeRequest;
		}

		public void setRoleTypeRequest(String roleType) {
			this.userTypeRequest = roleType;
		}

		public boolean isSentToManager() {
			return isSentToManager;
		}

		public void setSentToManager(boolean isSentToManager) {
			this.isSentToManager = isSentToManager;
		}

		public String getCreditCard() {
			return creditCard;
		}

		public void setCreditCard(String creditCard) {
			this.creditCard = creditCard;
		}

	}

	@FXML
	private TextField creditCardText;

	private Set<String> setID = new HashSet<>(); // set of ID's of existing users.

	ObservableList<ImportedUser> userInfo;

	private ArrayList<ImportedUser> userRows; /* userRows array list saves all the user which want to register and wern't yet send to manager for approval */

	private boolean exists; /*boolean which indicates if the user exists or not*/

	@FXML
	private TableView<ImportedUser> infoTable;

	@FXML
	private TableColumn<User, String> emailCol;

	@FXML
	private TableColumn<User, String> IDCol;

	@FXML
	private Text welcomeMessageText;

	@FXML
	private Text welcomeFirstName;

	@FXML
	private Text welcomeLastNameText;

	@FXML
	private Text welcomeIDText;

	@FXML
	private Text welcomePhoneNumberText;

	@FXML
	private Text welcomeEmailText;

	@FXML
	private Button BackButton;

	@FXML
	private TextField areaText;

	@FXML
	private TextField roleText;

	@FXML
	private Button CloseButton;

	@FXML
	private TextField firstNameText;

	@FXML
	private TextField lastNameText;

	@FXML
	private TextField telephoneText;

	@FXML
	private TextField emailText;

	@FXML
	private TextField IDText;

	@FXML
	private TextField userNameText;

	@FXML
	private TextField passwordText;

	@FXML
	private ComboBox<String> roleCombo;

	@FXML
	private TextField searchText;

	@FXML
	private Text errorMessageID;

	@FXML
	private Text msgRegister;

	@FXML
	private Button searchButton;

	@FXML
	private Button registerButton;

	@FXML
	/**
	 * method that triggers when the "Back" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	void Back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/ServiceRepresentativeInterface.fxml", event);
	}

	/**
	 * method that triggers when the "X" button has been pressed
	 * 
	 * @param event the ActionEvent that triggered this method call
	 */
	@FXML
	void close(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS", // DONE
					String.format("%s#", ClientUI.clientController.getUser().getUserName()), "PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
	}

	@FXML
	/**
	 * first checking user input and secondly sending the user to the manager. 
	 * @param event the ActionEvent that triggered this method call
	 */
	void register(ActionEvent event) {
		msgRegister.setVisible(false);
		ImportedUser currentUser = null;
		
		//finding the user in the userRows array list.
		for (ImportedUser user : userRows) {
			if (user.getID().equals(IDText.getText())) {
				currentUser = user;
				currentUser.setCreditCard(creditCardText.getText());
				break;
			}
		}

		//checking user input - if the user has been already sent to the manager - an error message will show up with the corresponding text.
		if (currentUser.isSentToManager()) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("this user has already been sent to the manager for approval!");
			return;
		}
		//checking user input - if the user already exists in the system (in users table) and his request is not upgrading himself to subscriber - 
		//an error message will show up with the corresponding text.
		if (setID.contains(IDText.getText()) && !(currentUser.userTypeRequest.equals("Registered To Subscriber"))) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("this user alredy exists!");
			return;
		}
		//checking user input - if the credit card text is empty - an error message will show up with the corresponding text.
		if (creditCardText.getText().isEmpty()) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("You need to fill the Credit Card number in order to complete registration!");
			return;
		}

		//checking user input - if the credit card text has leading zeros - an error message will show up with the corresponding text.
		if (creditCardText.getText().charAt(0) == '0') {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("You cannot have leading zeros!");
			return;
		}
		//checking user input - verifying that all the characters are digits.
		if (!isDigit(creditCardText.getText())) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("Credit Card number should contain only digits!");
			return;
		}
		//checking user input - verifying that the number of characters is exactly 16.
		if (creditCardText.getText().length() != 16) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("Credit Card number should have 16 digits!");
			return;
		}

		// showing a confirmation message for the employee to confirm that he wants to execute sending the user to the manager.
		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setContentText("Are you sure you want to send this user to manager?");

		Optional<ButtonType> result = conf.showAndWait();

		if (result.get() == ButtonType.OK) {

			//request a query to update the status of the request as sent to the manager, and update the credit card details.
			RequestObjectClient changeIsSentToManager = new RequestObjectClient("#UPDATE_REQUEST_TO_MANAGER_URC", 
					String.format("%s#%s#", IDText.getText(), creditCardText.getText()), "PUT");
			ClientUI.clientController.accept(changeIsSentToManager);

			
			//if the user is new:
			if (!setID.contains(currentUser.getID())) {
				//request query to add the new user to the users table.
				RequestObjectClient addToUsersTableRequest = new RequestObjectClient("#UPDATE_USERS_TABLE_URC", 
						String.format("%s#%s#%s#%s#%s#%s#%s#%s#", currentUser.getFirstName(), currentUser.getLastName(),
								currentUser.getPhone(), currentUser.getEmail(), currentUser.getID(),
								currentUser.getUserName(), currentUser.getPassword(), currentUser.getArea()),
						"POST");
				ClientUI.clientController.accept(addToUsersTableRequest);

				//request query to add the new user to "registerclientstable" table with his name and credit card details.
				RequestObjectClient addUsersToRegisteredClientsTable = new RequestObjectClient( 
						"#UPDATE_REGISTERED_CLIENTS_TABLE_URC",
						String.format("%s#%s#", currentUser.getUserName(), currentUser.getCreditCard()), "POST");
				ClientUI.clientController.accept(addUsersToRegisteredClientsTable);
			} else {
				//if the user is not new, but he is upgrading from registered to subscriber:
				
				//request query to change the user status in the "registerclientstable" table 
				RequestObjectClient updateExistingUserInRegClientTable = new RequestObjectClient(
						"#UPDATE_REQUEST_IN_REG_CLIENTS_URC",
						String.format("%s#%s#%s#", currentUser.getUserName(), currentUser.getRoleTypeRequest(),
								currentUser.getCreditCard()),
						"PUT");
				ClientUI.clientController.accept(updateExistingUserInRegClientTable);
			}

			
			/**
			 * showing an alert to inform the employee about changes success.
			 */
			Alert info = new Alert(AlertType.INFORMATION);
			info.setContentText("User has been updated in users table!");
			info.showAndWait();

			//setting the text of information about successful completion of the action to be visible.
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.GREEN);
			msgRegister.setText("User sent to manager successfully!");

			userRows.clear();
			clearAllTextFields();
			registerButton.setDisable(true);
			creditCardText.setDisable(true);

			// query request to get all the users which were sent to the manager.
			RequestObjectClient getUsers = new RequestObjectClient("#GET_USERS_URC",
					"", "GET");
			ClientUI.clientController.accept(getUsers);

			userInfo = FXCollections.observableArrayList(userRows);
			this.infoTable.setItems(userInfo);
		}

	}

	/**
	 * checks that all characters of the string are digits;
	 * 
	 * @param s some user String
	 * @return true if all the characters are digits, else return false.
	 */
	private boolean isDigit(String s) {
		for (char c : s.toCharArray()) {
			int cInt = Character.getNumericValue(c);
			if (cInt < 0 || cInt > 9) {
				return false;
			}
		}
		return true;
	}

	@FXML
	/**
	 * invoked when the user clicks on the search button.
	 * uses the searchText in which the user enters the ID of the user he searches.
	 * searches for the user by the ID input in the searchText.
	 * if the user is found: his data is presented in the text fields on the right side of the screen.
	 * else an error message is shown saying that this user does not exist in the list.
	 * @param event
	 */
	void searchUser(ActionEvent event) {
		errorMessageID.setVisible(false);
		String getUserText = searchText.getText();
		
		//checking the user input - verifying that the input is not empty.
		if (getUserText.isEmpty()) {
			clearAllTextFields();
			creditCardText.setDisable(true);
			errorMessageID.setText("Please enter user ID");
			errorMessageID.setVisible(true);
			return;
		}
		//checking user input - verifying that there aren't leading zeros.
		if (getUserText.charAt(0) == '0') {
			clearAllTextFields();
			creditCardText.setDisable(true);
			errorMessageID.setText("You can not have leading zeroes");
			errorMessageID.setVisible(true);
			return;
		}
		//checking user input - verifying that all the characters are digits.
		if (!isDigit(getUserText)) {
			clearAllTextFields();
			creditCardText.setDisable(true);
			errorMessageID.setText("User ID must consist of numbers only!");
			errorMessageID.setVisible(true);
			return;
		}

		//searching the current user by the ID in the userRows (which stores all the users with registration requests).
		ImportedUser currentUser = null;
		for (ImportedUser user : userRows) {
			if (user.getID().equals(getUserText)) {
				currentUser = user;
				exists = true;
				break;
			}
		}

		///if the user exists - his data is shown in the text fields on the right side of the screen.
		if (exists) {
			registerButton.setDisable(false);
			creditCardText.setDisable(false);

			firstNameText.setText(currentUser.getFirstName());
			lastNameText.setText(currentUser.getLastName());
			telephoneText.setText(currentUser.getPhone());
			emailText.setText(currentUser.getEmail());
			IDText.setText(currentUser.getID());
			userNameText.setText(currentUser.getUserName());
			passwordText.setText(currentUser.getPassword());

			areaText.setText(currentUser.getArea());
			roleText.setText(currentUser.getRoleTypeRequest());
			exists = false;
			return;
		} else {
			//the user was not found, so error message will show up.
			clearAllTextFields();
			errorMessageID.setText(String.format("user with ID: %s does not exist", getUserText));
			errorMessageID.setVisible(true);
			creditCardText.setDisable(true);
		}
	}

	/**
	 * clearing all the text field from text.
	 */
	private void clearAllTextFields() {
		firstNameText.clear();
		lastNameText.clear();
		telephoneText.clear();
		emailText.clear();
		IDText.clear();
		userNameText.clear();
		passwordText.clear();
		areaText.clear();
		roleText.clear();
		searchText.clear();
		creditCardText.clear();
	}

	@Override
	/** 
	 * saving all the data which is returned from the DB and relevant for the current controller.
	 * saves all the id's of the existing users in the setID.
	 * saves all imported users in userRows.
	 */
	public void updatedata(Object data) {
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_EXISTING_USERS":

				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String ID = (String) values[0];
						//adding the user ID to setID.
						setID.add(ID);
					}
				}
				break;
			case "#GET_USERS_URC":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String firstName = (String) values[0];
						String LastName = (String) values[1];
						String Telephone = (String) values[2];
						String Email = (String) values[3];
						String ID = (String) values[4];
						String userName = (String) values[5];
						String userPassword = (String) values[6];
						String area = (String) values[7];
						String userTypeRequest = (String) values[8];
						boolean isSentToManager = (Boolean) values[9];

						//saving all the data of the imported user in the ImportedUser object.
						User curr = new User(firstName, LastName, Telephone, Email, ID, userName, userPassword, area);
						ImportedUser user = new ImportedUser(curr, area, userTypeRequest, isSentToManager);
						//adding this retrieved user to the user rows array list.
						userRows.add(user);
					}
				}
				break;
			}
		}
	}

	@Override
	/**
	 * initialises the controller as it is loaded. 
	 */
	public void initialize(URL location, ResourceBundle resources) {
		// setting the static clientController to be this controller.
		ClientUI.clientController.setController(this);
		
		//setting all the texts and buttons to be disabled and hiding the registration message text 
		msgRegister.setVisible(false);
		registerButton.setDisable(true);
		firstNameText.setDisable(true);
		lastNameText.setDisable(true);
		telephoneText.setDisable(true);
		emailText.setDisable(true);
		IDText.setDisable(true);
		userNameText.setDisable(true);
		passwordText.setDisable(true);
		areaText.setDisable(true);
		roleText.setDisable(true);
		creditCardText.setDisable(true);

		// initialising a pattern of maximum 10 chars.
		Pattern pattern1 = Pattern.compile(".{0,10}");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		TextFormatter formatter1 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern1.matcher(change.getControlNewText()).matches() ? change : null;
		});
		// initialising a pattern of maximum 16 chars.
		Pattern pattern2 = Pattern.compile(".{0,16}");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
		});

		// restricting the number of input characters in the text label in the
		// searchText Text to maximum of 10 character.
		searchText.setTextFormatter(formatter1);
		// restricting the number of input characters in the text label in the
		// searchText Text to maximum of 16 character.
		creditCardText.setTextFormatter(formatter2);

		// updating all the info about the current logged in user, in the Text elements
		// of the GUI.
		welcomeFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		welcomeLastNameText.setText(ClientUI.clientController.getUser().getLastName());
		welcomeIDText.setText(ClientUI.clientController.getUser().getID());
		welcomePhoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		welcomeEmailText.setText(ClientUI.clientController.getUser().getEmail());
		welcomeMessageText
				.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));
		exists = false;
		errorMessageID.setVisible(false);

		userRows = new ArrayList<>();

		// setting the columns of the table.
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("Email"));
		IDCol.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));

		// query request to get the ID's of existing users in the DB.
		RequestObjectClient getExistingUsers = new RequestObjectClient("#GET_EXISTING_USERS_URC", "", "GET");
		ClientUI.clientController.accept(getExistingUsers);

		// query request to get all the users which were sent to the manager.
		RequestObjectClient getUsers = new RequestObjectClient("#GET_USERS_URC", "", "GET");
		ClientUI.clientController.accept(getUsers);

		userInfo = FXCollections.observableArrayList(userRows);
		this.infoTable.setItems(userInfo);
		this.infoTable.refresh();
	}

}