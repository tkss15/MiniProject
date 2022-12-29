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

	private ArrayList<ImportedUser> userRows;
	private ImportedUser user;

	@FXML
	private TableView<ImportedUser> infoTable;

	@FXML
	private TableColumn<User, String> emailCol;

	@FXML
	private TableColumn<User, String> IDCol;

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

	}

	@FXML
	void searchUser(ActionEvent event) {
		String getUserText = searchText.getText();
		RequestObjectClient searchUserInDB = new RequestObjectClient("#SEARCH_USER",
				String.format("table=importtable#condition=ID=%s", getUserText), "GET");
		ClientUI.clientController.accept(searchUserInDB);
		
		firstNameText.setText(user.getFirstName());
		lastNameText.setText(user.getLastName());
		telephoneText.setText(user.getPhone());
		emailText.setText(user.getEmail()); 
		IDText.setText(user.getID());
		userNameText.setText(user.getUserName());
		passwordText.setText(user.getPassword());
		
		areaText.setText(user.getArea());
		roleText.setText(user.getRoleType());

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
						User curr = new User(firstName, LastName, Telephone, Email, ID, userName, userPassword);
						user = new ImportedUser(curr, area, userType);
						userRows.add(user);
					}
				}
				break;
			case "#SEARCH_USER":

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
						User curr = new User(firstName, LastName, Telephone, Email, ID, userName, userPassword);
						user = new ImportedUser(curr, area, userType);
					}
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		errorMessage.setVisible(false);
		ClientUI.clientController.setController(this);
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("Email"));
		IDCol.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));
		RequestObjectClient getUsers = new RequestObjectClient("#GET_USERS", "table=importtable", "GET");
		ClientUI.clientController.accept(getUsers);
		
		final ObservableList<ImportedUser> userInfo = FXCollections.observableArrayList(userRows);
		this.infoTable.setItems(userInfo);
		this.infoTable.refresh();

	}

}
