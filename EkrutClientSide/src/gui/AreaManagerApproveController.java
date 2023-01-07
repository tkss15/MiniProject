package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Facility;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class AreaManagerApproveController implements Initializable, IController {

	public class UserRow {
		private String ID;
		private String name;
		private String registrationsType;
		private ComboBox<String> status;

		public UserRow(String ID, String name, String registrationsType, ComboBox<String> status) {
			this.ID = ID;
			this.name = name;
			this.registrationsType = registrationsType;
			this.status = status;
		}

		public String getID() {
			return ID;
		}

		public void setID(String ID) {
			this.ID = ID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getRegistrationStype() {
			return registrationsType;
		}

		public void setRegistrationStype(String registrationStype) {
			this.registrationsType = registrationStype;
		}

		public ComboBox<String> getStatus() {
			return status;
		}

		public void setStatus(ComboBox<String> status) {
			this.status = status;
		}

	}
	
	
	private User user;
	private ArrayList<UserRow> userRows;
	@FXML
	private Text textUserlogin;

	@FXML
	private Text textFirstName;

	@FXML
	private Text textLastName;

	@FXML
	private Text textID;

	@FXML
	private Text textTelephone;

	@FXML
	private Text textEmail;

	@FXML
	private HBox textBranch;

	@FXML
	private Button Back;

	@FXML
	private Button CloseButton;

	@FXML
	private TableView<UserRow> ApproveTable;

	@FXML
	private TableColumn<UserRow, String> IDColumn;

	@FXML
	private TableColumn<UserRow, String> NameColumn;

	@FXML
	private TableColumn<UserRow, String> RegistrationTypeColumn;

	@FXML
	private TableColumn<UserRow, ComboBox<String>> StatusColumn;

	@FXML
	private Button changesBTN;

	@FXML
	void Back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}

	@FXML
	void applyChangesToTable(ActionEvent event) {
		for (int i = 0; i < userRows.size(); i++) {
			UserRow currRow = userRows.get(i);
			if (currRow.status.getValue().equals("Reject")) {
				RequestObjectClient RejectUser = new RequestObjectClient("#REJECT_USER",
						String.format("table=importtable#condition=ID=%s", currRow.ID), "DELETE");
				ClientUI.clientController.accept(RejectUser);
			}

			if (currRow.status.getValue().equals("Approve")) {
				if (currRow.registrationsType.equals("RegisteredClient")) {
					RequestObjectClient getCurrUser = new RequestObjectClient("#GET_CURR_USER",
							String.format("table=importtable#condition=ID=%s", currRow.ID), "GET");
					ClientUI.clientController.accept(getCurrUser);

					RequestObjectClient ApproveUserToUsers = new RequestObjectClient("#POST_TO_USERS",
							String.format("table=users#values=firstName=%s&"
									+ "lastName=%s&"
									+ "telephone=%s&"
									+ "Email=%s&"
									+ "ID=%s&"
									+ "userName=%s&"
									+ "userPassword=%s&"
									+ "userOnline=Offline&"
									+ "Area=%s", user.getFirstName(),user.getLastName(),
									user.getPhone(),user.getEmail(),user.getID(),user.getUserName()
									,user.getPassword(),user.getArea()), "POST");
					
					ClientUI.clientController.accept(ApproveUserToUsers);
					
					RequestObjectClient ApproveUsertoClients = new RequestObjectClient("#APPROVE_USER",
							String.format("table=registerclients#values=userName=%s", user.getUserName()), "POST");
					
					ClientUI.clientController.accept(ApproveUsertoClients);

					RequestObjectClient RemoveAfterApproval = new RequestObjectClient("#DELETE_AFTER_APPROVAL",
							String.format("table=importtable#condition=ID=%s", currRow.ID), "DELETE");
					
					ClientUI.clientController.accept(RemoveAfterApproval);
				}
				
				if (currRow.registrationsType.equals("SubscribedClient")) {
					RequestObjectClient getCurrUser = new RequestObjectClient("#GET_CURR_USER",
							String.format("table=importtable#condition=ID=%s", currRow.ID), "GET");
					ClientUI.clientController.accept(getCurrUser);

					RequestObjectClient ApproveUserToUsers = new RequestObjectClient("#POST_TO_USERS",
							String.format("table=users#values=firstName=%s&"
									+ "lastName=%s&"
									+ "telephone=%s&"
									+ "Email=%s&"
									+ "ID=%s&"
									+ "userName=%s&"
									+ "userPassword=%s&"
									+ "userOnline=Offline&"
									+ "Area=%s", user.getFirstName(),user.getLastName(),
									user.getPhone(),user.getEmail(),user.getID(),user.getUserName()
									,user.getPassword(),user.getArea()), "POST");
					
					ClientUI.clientController.accept(ApproveUserToUsers);
					
					RequestObjectClient ApproveUsertoClients = new RequestObjectClient("#APPROVE_USER",
							String.format("table=registerclients#values=userName=%s", user.getUserName()), "POST");
					
					ClientUI.clientController.accept(ApproveUsertoClients);
					
					RequestObjectClient ApproveUsertoSubscribers = new RequestObjectClient("#APPROVE_USER",
							String.format("table=subscribers#values=userName=%s&", user.getUserName()), "POST");
					ClientUI.clientController.accept(ApproveUsertoSubscribers);

					RequestObjectClient RemoveAfterApproval = new RequestObjectClient("#DELETE_AFTER_APPROVAL",
							String.format("table=importtable#condition=ID=%s", currRow.ID), "DELETE");
					
					ClientUI.clientController.accept(RemoveAfterApproval);
				}
				

			}
		}
		ApproveTable.refresh();
	}

	@FXML
	void closeWindow(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("table=users#condition=userName=%s#values=userOnline=\"Offline\"",
							ClientUI.clientController.getUser().getUserName()),
					"PUT");
			ClientUI.clientController.accept(request);
			ClientUI.clientController.getUser().setOnlineStatus("Offline");
		}
		System.exit(0);
	}

	@Override
	public void updatedata(Object data) {
		System.out.println(111111);
		if (data instanceof ResponseObject) {
			ResponseObject serverResponse = (ResponseObject) data;
			switch (serverResponse.getRequest()) {
			case "#GET_USERS":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String ID = (String) values[4];
						String firstName = (String) values[0];
						String lastName = (String) values[1];
						String registrationsType = (String) values[8];
						UserRow ur = new UserRow(ID, firstName + " " + lastName, registrationsType, null);
						userRows.add(ur);
					}
				}
				break;
			case "#GET_CURR_USER":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						String firstName = (String) values[0];
						String lastName = (String) values[1];
						String telephone = (String) values[2];
						String Email = (String) values[3];
						String ID = (String) values[4];
						String userName = (String) values[5];
						String userPassword = (String) values[6];
						String area = (String) values[7];
						User user = new User(firstName, lastName, telephone, Email, ID, userName, userPassword, area);
					}
				}
				break;
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.clientController.setController(this);
		userRows = new ArrayList<>();
		textFirstName.setText(ClientUI.clientController.getUser().getFirstName());
		textLastName.setText(ClientUI.clientController.getUser().getLastName());
		textID.setText(ClientUI.clientController.getUser().getID());
		textTelephone.setText(ClientUI.clientController.getUser().getPhone());
		textEmail.setText(ClientUI.clientController.getUser().getEmail());
		textUserlogin.setText(String.format("Welcome Back %s", ClientUI.clientController.getUser().getFirstName()));

		RequestObjectClient requestUsersFromImportTable = new RequestObjectClient("#GET_USERS",
				String.format("table=importtable#condition=area=%s&isSentToManager=1",
						ClientUI.clientController.getUser().getArea()),
				"GET");
		ClientUI.clientController.accept(requestUsersFromImportTable);
		for (UserRow ur : userRows) {
			ComboBox<String> combo = new ComboBox<>();
			combo.setPromptText("Choose Accept / Reject");
			ObservableList<String> info = FXCollections.observableArrayList("Approve", "Reject");
			combo.setItems(info);
			ur.setStatus(combo);
		}
		IDColumn.setCellValueFactory(new PropertyValueFactory<UserRow, String>("ID"));
		NameColumn.setCellValueFactory(new PropertyValueFactory<UserRow, String>("name"));
		RegistrationTypeColumn.setCellValueFactory(new PropertyValueFactory<UserRow, String>("registrationStype"));
		StatusColumn.setCellValueFactory(new PropertyValueFactory<UserRow, ComboBox<String>>("status"));

		ObservableList<UserRow> info = FXCollections.observableArrayList(userRows);
		ApproveTable.setItems(info);

	}

}
