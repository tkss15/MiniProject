package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import Entity.User;
import client.ClientUI;
import common.IController;
import common.RequestObjectClient;
import common.ResponseObject;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class UsersRegistrationController implements Initializable, IController {

	public class ImportedUser extends User {

		private String userTypeRequest;
		private boolean isSentToManager;
		private String creditCard;

		public ImportedUser(User user, String area, String userTypeRequest, boolean isSentToManager) {
			super(user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail(), user.getID(),
					user.getUserName(), user.getPassword(), user.getArea());

			this.userTypeRequest = userTypeRequest;
			this.isSentToManager = isSentToManager;
		}

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

	private Set<String> setID = new HashSet<>(); // set of ID's of existing users.
	ObservableList<ImportedUser> userInfo;

	private ArrayList<ImportedUser> userRows;

	private boolean exists;

//	@FXML
//	private TableView<ImportedUser> infoTable;
//
//	@FXML
//	private TableColumn<User, String> emailCol;
//
//	@FXML
//	private TableColumn<User, String> IDCol;

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
	private Button CloseButton;

	@FXML
	private TextField firstNameText;

	@FXML
	private Text firstNameLabel;

	@FXML
	private TextField lastNameText;

	@FXML
	private Text lastNameLabel;

	@FXML
	private TextField telephoneText;

	@FXML
	private Text telephoneLabel;

	@FXML
	private TextField emailText;

	@FXML
	private Text emailLabel;

	@FXML
	private TextField IDText;

	@FXML
	private Text idLabel;

	@FXML
	private TextField userNameText;

	@FXML
	private Text userNameLabel;

	@FXML
	private TextField passwordText;

	@FXML
	private Text passwordLabel;

	@FXML
	private Text areaLabel;

	@FXML
	private Text userTypeLabel;

	@FXML
	private TextField searchText;

	@FXML
	private Text errorMessageID;

	@FXML
	private Button searchButton;

	@FXML
	private Button registerButton;

	@FXML
	private TextField areaText;

	@FXML
	private TextField roleText;

	@FXML
	private TextField creditCardText;

	@FXML
	private Text creditCardLabel;

	@FXML
	private Text msgRegister;

	@FXML
	private ImageView creditCardImage;

	@FXML
	private ImageView pencilImageUserType;

	@FXML
	private ImageView pencilImageUserArea;

	@FXML
	private ImageView pencilImageFirstName;

	@FXML
	private ImageView pencilImageLasName;

	@FXML
	private ImageView telephoneImage;

	@FXML
	private ImageView emailImage;

	@FXML
	private ImageView lockImage;

	@FXML
	private ImageView userImage;

	@FXML
	private ImageView pencilImageId;

	@FXML
	void Back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/ServiceRepresentativeInterface.fxml", event);
	}

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
	void register(ActionEvent event) {
		msgRegister.setVisible(false);
		ImportedUser currentUser = null;
		for (ImportedUser user : userRows) {
			if (user.getID().equals(IDText.getText())) {
				currentUser = user;
				currentUser.setCreditCard(creditCardText.getText());
				break;
			}
		}

		if (currentUser.isSentToManager()) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("this user has already been sent to the manager for approval!");
			return;
		}

		if (setID.contains(IDText.getText()) && !(currentUser.userTypeRequest.equals("Registered To Subscriber"))
				&& !(currentUser.userTypeRequest.equals("Subscriber To Registered"))) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("this user alredy exists!");
			return;
		}

		if (creditCardText.getText().isEmpty()) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("You need to fill the Credit Card number in order to complete registration!");
			return;
		}

		if (creditCardText.getText().charAt(0) == '0') {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("You cannot have leading zeros!");
			return;
		}

		if (!isDigit(creditCardText.getText())) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("Credit Card number should contain only digits!");
			return;
		}
		if (creditCardText.getText().length() != 16) {
			msgRegister.setVisible(true);
			msgRegister.setFill(Color.RED);
			msgRegister.setText("Credit Card number should have 16 digits!");
			return;
		}

		Alert conf = new Alert(AlertType.CONFIRMATION);
		conf.setContentText("Are you sure you want to send this user to manager?");

		Optional<ButtonType> result = conf.showAndWait();

		if (result.get() == ButtonType.OK) {

			// set isSentToManager = 1.
			RequestObjectClient changeIsSentToManager = new RequestObjectClient("#UPDATE_REQUEST_TO_MANAGER_URC", // DONE
					String.format("%s#%s#", IDText.getText(), creditCardText.getText()), "PUT");
			ClientUI.clientController.accept(changeIsSentToManager);

//			POST - -
//			 * table=subscriber#values=id=3&username=tkss15&lastname=shneor

			if (!setID.contains(currentUser.getID())) {
				RequestObjectClient addToUsersTableRequest = new RequestObjectClient("#UPDATE_USERS_TABLE_URC", // DONE
						String.format("%s#%s#%s#%s#%s#%s#%s#%s#", currentUser.getFirstName(), currentUser.getLastName(),
								currentUser.getPhone(), currentUser.getEmail(), currentUser.getID(),
								currentUser.getUserName(), currentUser.getPassword(), currentUser.getArea()),
						"POST");
				ClientUI.clientController.accept(addToUsersTableRequest);

				RequestObjectClient addUsersToRegisteredClientsTable = new RequestObjectClient( // DONE
						"#UPDATE_REGISTERED_CLIENTS_TABLE_URC",
						String.format("%s#%s#", currentUser.getUserName(), currentUser.getCreditCard()), "POST");
				ClientUI.clientController.accept(addUsersToRegisteredClientsTable);
			} else {
				RequestObjectClient updateExistingUserInRegClientTable = new RequestObjectClient(
						"#UPDATE_REQUEST_IN_REG_CLIENTS_URC", // DONE
						String.format("%s#%s#%s#", currentUser.getUserName(), currentUser.getRoleTypeRequest(),
								currentUser.getCreditCard()),
						"PUT");
				ClientUI.clientController.accept(updateExistingUserInRegClientTable);
			}

			Alert info = new Alert(AlertType.INFORMATION);
			info.setContentText("User has been updated in users table!");
			info.showAndWait();

			userRows.clear();
			clearAllTextFields();
			registerButton.setDisable(true);
			creditCardText.setDisable(true);

			RequestObjectClient getUsers = new RequestObjectClient("#GET_USERS_URC", // DONE
					"", "GET");
			ClientUI.clientController.accept(getUsers);
			
			setDetailsVisible(false);

//			userInfo = FXCollections.observableArrayList(userRows);
//			this.infoTable.setItems(userInfo);
		}

	}

	// checks that all characters of the string are digits;
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
	void searchUser(ActionEvent event) {
		errorMessageID.setVisible(false);
		String getUserText = searchText.getText();
		if (getUserText.isEmpty()) {
			clearAllTextFields();
			setDetailsVisible(false);
			creditCardText.setDisable(true);
			errorMessageID.setText("Please enter user ID");
			errorMessageID.setVisible(true);
			return;
		}
		if (getUserText.charAt(0) == '0') {
			clearAllTextFields();
			setDetailsVisible(false);
			creditCardText.setDisable(true);
			errorMessageID.setText("You can not have leading zeroes");
			errorMessageID.setVisible(true);
			return;
		}
		if (!isDigit(getUserText)) {
			clearAllTextFields();
			setDetailsVisible(false);
			creditCardText.setDisable(true);
			errorMessageID.setText("User ID must consist of numbers only!");
			errorMessageID.setVisible(true);
			return;
		}

		ImportedUser currentUser = null;
		for (ImportedUser user : userRows) {
			if (user.getID().equals(getUserText)) {
				currentUser = user;
				exists = true;
				break;
			}
		}

		if (exists) {
			registerButton.setDisable(false);
			creditCardText.setDisable(false);

			setDetailsVisible(true);

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
			setDetailsVisible(false);
			clearAllTextFields();
			errorMessageID.setText(String.format("user with ID: %s does not exist", getUserText));
			errorMessageID.setVisible(true);
			creditCardText.setDisable(true);
		}
		// table=users#condition=userName=%s#values=userName=username&userPassword=password
	}

	/**
	 * sets all the GUI elements of on the screen according to the boolean flag.
	 * if flag = false -> all the elements will be hidden, else the flag will be true.
	 * @param flag the boolean variable of the visible property of the GUI elements.
	 */
	private void setDetailsVisible(boolean flag) {
		registerButton.setVisible(flag);
		firstNameText.setVisible(flag);
		lastNameText.setVisible(flag);
		telephoneText.setVisible(flag);
		emailText.setVisible(flag);
		IDText.setVisible(flag);
		userNameText.setVisible(flag);
		passwordText.setVisible(flag);
		areaText.setVisible(flag);
		roleText.setVisible(flag);
		creditCardText.setVisible(flag);
		
		pencilImageFirstName.setVisible(flag);
		pencilImageId.setVisible(flag);
		pencilImageLasName.setVisible(flag);
		pencilImageUserArea.setVisible(flag);
		pencilImageUserType.setVisible(flag);
		emailImage.setVisible(flag);
		userImage.setVisible(flag);
		telephoneImage.setVisible(flag);
		lockImage.setVisible(flag);
		creditCardImage.setVisible(flag);
		
		
		firstNameLabel.setVisible(flag);
		lastNameLabel.setVisible(flag);
		telephoneLabel.setVisible(flag);
		emailLabel.setVisible(flag);
		idLabel.setVisible(flag);
		userNameLabel.setVisible(flag);
		passwordLabel.setVisible(flag);
		areaLabel.setVisible(flag);
		userTypeLabel.setVisible(flag);
		creditCardLabel.setVisible(flag);
		
		registerButton.setVisible(flag);
	}

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
	public void updatedata(Object data) {
		System.out.println("UserRegistrationController");
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_EXISTING_USERS":

				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String ID = (String) values[0];
						setID.add(ID);
					}
				}
				break;
			case "#GET_USERS_URC":
				System.out.println(serverResponse.Responsedata.size());
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

						User curr = new User(firstName, LastName, Telephone, Email, ID, userName, userPassword, area);
						ImportedUser user = new ImportedUser(curr, area, userTypeRequest, isSentToManager);
						userRows.add(user);
					}
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		msgRegister.setVisible(false);

		setDetailsVisible(false);

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

		Pattern pattern1 = Pattern.compile(".{0,10}");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		TextFormatter formatter1 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern1.matcher(change.getControlNewText()).matches() ? change : null;
		});
		Pattern pattern2 = Pattern.compile(".{0,16}");
		@SuppressWarnings({ "rawtypes", "unchecked" })
		TextFormatter formatter2 = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
			return pattern2.matcher(change.getControlNewText()).matches() ? change : null;
		});

		searchText.setTextFormatter(formatter1);
		creditCardText.setTextFormatter(formatter2);

		welcomeFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		welcomeLastNameText.setText(ClientUI.clientController.getUser().getLastName());
		welcomeIDText.setText(ClientUI.clientController.getUser().getID());
		welcomePhoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		welcomeEmailText.setText(ClientUI.clientController.getUser().getEmail());
		welcomeMessageText
				.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));
		exists = false;
		errorMessageID.setVisible(false);
		ClientUI.clientController.setController(this);

		userRows = new ArrayList<>();

//		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("Email"));
//		IDCol.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));

		// get the ID's of existing users in the DB.
		RequestObjectClient getExistingUsers = new RequestObjectClient("#GET_EXISTING_USERS_URC", // DONE
				"", "GET");
		ClientUI.clientController.accept(getExistingUsers);

		RequestObjectClient getUsers = new RequestObjectClient("#GET_USERS_URC", "", "GET");
		ClientUI.clientController.accept(getUsers);

//		userInfo = FXCollections.observableArrayList(userRows);
//		this.infoTable.setItems(userInfo);
//		this.infoTable.refresh();
	}

}