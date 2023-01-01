package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.ImportedUser;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class UsersRegistrationController implements Initializable, IController {
	ObservableList<ImportedUser> userInfo;
	private ArrayList<ImportedUser> userRows;
	private ImportedUser user;

	private boolean exists;

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
	private Text errorMessage;

	@FXML
	private Button searchButton;

	@FXML
	private Button registerButton;

	@FXML
	void Back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		ClientUI.sceneManager.ShowScene("../views/ServiceRepresentativeInterface.fxml");
	}

	@FXML
	void chooseRole(ActionEvent event) {

	}

	@FXML
	void close(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void register(ActionEvent event) {
		if(user.isSentToManager()) {
			return;
		}

		RequestObjectClient changeIsSentToManager = new RequestObjectClient("#UPDATE_REQUEST_TO_MANAGER",
				String.format("table=importtable#condition=ID=%s#values=isSentToManager=1", user.getID()), "PUT");
		ClientUI.clientController.accept(changeIsSentToManager);
		RequestObjectClient getUsersAfterUpdate = new RequestObjectClient("#GET_USERS",
				"table=importtable#condition=isSentToManager=0", "GET");
		ClientUI.clientController.accept(getUsersAfterUpdate);
		this.infoTable.getItems().clear();
		userInfo = FXCollections.observableArrayList(userRows);
		this.infoTable.setItems(userInfo);
		this.infoTable.refresh();
		
		
		
	}

	@FXML
	void searchUser(ActionEvent event) {
		errorMessage.setVisible(false);
		String getUserText = searchText.getText();
		if (getUserText.isEmpty()) {
			errorMessage.setText("Please enter user ID");
			errorMessage.setVisible(true);
			return;
		}
		RequestObjectClient searchUserInDB = new RequestObjectClient("#SEARCH_USER",
				String.format("table=importtable#condition=ID=%s&isSentToManager=0", getUserText), "GET");
		ClientUI.clientController.accept(searchUserInDB);

		if (exists) {
			firstNameText.setText(user.getFirstName());
			lastNameText.setText(user.getLastName());
			telephoneText.setText(user.getPhone());
			emailText.setText(user.getEmail());
			IDText.setText(user.getID());
			userNameText.setText(user.getUserName());
			passwordText.setText(user.getPassword());

			areaText.setText(user.getArea());
			roleText.setText(user.getRoleType());
			exists = false;
			return;
		}
		else {
			firstNameText.setText(null);
			lastNameText.setText(null);
			telephoneText.setText(null);
			emailText.setText(null);
			IDText.setText(null);
			userNameText.setText(null);
			passwordText.setText(null);

			areaText.setText(null);
			roleText.setText(null);
			
			errorMessage.setText(String.format("user with ID: %s does not exist", getUserText));
			errorMessage.setVisible(true);
		}
		// table=users#condition=userName=%s#values=userName=username&userPassword=password
	}

	@Override
	public void updatedata(Object data) {
		System.out.println("UserRegistrationController");
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_USERS":
				userRows = new ArrayList<>();

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
						String userType = (String) values[8];
						boolean isSentToUser = (Boolean)values[9];
						User curr = new User(firstName, LastName, Telephone, Email, ID, userName, userPassword,area);
						user = new ImportedUser(curr, area, userType,isSentToUser);
						userRows.add(user);
					}
				}
				break;
			case "#SEARCH_USER":
				System.out.println(serverResponse.Responsedata.size());
				if (serverResponse.Responsedata.size() != 0) {
					exists = true;
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
						String userType = (String) values[8];
						boolean isSentToUser = (Boolean)values[9];
						
						User curr = new User(firstName, LastName, Telephone, Email, ID, userName, userPassword,area);
						user = new ImportedUser(curr, area, userType,isSentToUser);
					}
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		welcomeFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		welcomeLastNameText.setText(ClientUI.clientController.getUser().getLastName());
		welcomeIDText.setText(ClientUI.clientController.getUser().getID());
		welcomePhoneNumberText.setText(ClientUI.clientController.getUser().getPhone());
		welcomeEmailText.setText(ClientUI.clientController.getUser().getEmail());
		welcomeMessageText.setText(String.format("Welcome Back %s",ClientUI.clientController.getUser().getFirstName()));
		exists = false;
		errorMessage.setVisible(false);
		ClientUI.clientController.setController(this);
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("Email"));
		IDCol.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));
		RequestObjectClient getUsers = new RequestObjectClient("#GET_USERS",
				"table=importtable#condition=isSentToManager=0", "GET");
		ClientUI.clientController.accept(getUsers);

		userInfo = FXCollections.observableArrayList(userRows);
		this.infoTable.setItems(userInfo);
		this.infoTable.refresh();
	}

}