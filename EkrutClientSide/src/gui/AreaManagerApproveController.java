package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AreaManagerApproveController implements Initializable, IController {
	
	/**
	 * this class extends the User class.
	 * inner class for representation of the rows of the table.
	 * ImportedUser saves information about users which want to register to our system.
	 * ImportedUser saves all of user's attributes (as it extends the User class) but also it saves the user credit card, his registration status -
	 * which is to be decided by the manager, it saves a boolean which shows if the user was sent to manager for acception, and it also saves the registration type of
	 * that user - which can be one of the following ('Registered','Subscriber','Registered To Subscriber').
	 * 
	 * @author galmu
	 *
	 */
	public class ImportedUser extends User {

		private boolean isSentToManager;
		private String creditCard;
		private ComboBox<String> status;
		private String registrationType;

		/**
		 * constructor of the ImportedUserd class.
		 * @param user
		 * @param area
		 * @param isSentToManager
		 * @param registrationsType
		 * @param status
		 * @param cardNumber
		 */
		public ImportedUser(User user, String area, boolean isSentToManager, String registrationsType,
				ComboBox<String> status, String cardNumber) {
			super(user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail(), user.getID(),
					user.getUserName(), user.getPassword(), user.getArea());

			this.isSentToManager = isSentToManager;
			this.status = status;
			this.registrationType = registrationsType;
			this.creditCard = cardNumber;
		}
		
		/*getters and setters*/

		public String getRegistrationType() {
			return registrationType;
		}

		public void setRegistrationType(String registrationType) {
			this.registrationType = registrationType;
		}

		public ComboBox<String> getStatus() {
			return status;
		}

		public void setStatus(ComboBox<String> status) {
			this.status = status;
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

	//priority queue - max heap - for assigning the the next subscriber number for a new subscriber, as subscriber numbers are following integers: 0,1,2...
	private PriorityQueue<Integer> subNums = new PriorityQueue<Integer>(Collections.reverseOrder());

	//ArrayList which is used to store all the users which want to register. 
	//this array list saves all the user rows to be shown in the table.
	private ArrayList<ImportedUser> userRows;
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

	//declaring the table and its columns.
	@FXML
	private TableView<ImportedUser> ApproveTable;

	@FXML
	private TableColumn<User, String> IDColumn;

	@FXML
	private TableColumn<User, String> NameColumn;

	@FXML
	private TableColumn<ImportedUser, String> RegistrationTypeColumn;

	@FXML
	private TableColumn<ImportedUser, ComboBox<String>> StatusColumn;

	@FXML
	private Button changesBTN;

	@FXML
	/**
	 * returns to the previous window via the SceneManager class.
	 * @param event
	 */
	void Back(ActionEvent event) {
		ClientUI.sceneManager.ShowSceneNew("../views/AreaManagerInterface.fxml", event);
	}

	@FXML
	void applyChangesToTable(ActionEvent event) {
		for (int i = 0; i < userRows.size(); i++) {
			ImportedUser currRow = userRows.get(i);

			if (currRow.getStatus().getValue() != null) {

				if (currRow.getStatus().getValue().equals("Reject")) {
					if (!currRow.getRegistrationType().equals("Registered To Subscriber")) {
						Alert conf = new Alert(AlertType.CONFIRMATION);
						conf.setContentText("Are you sure you want to reject " + currRow.getFirstName() + " "
								+ currRow.getLastName() + ", and delete this user permanently?");
						Optional<ButtonType> result = conf.showAndWait();

						if (result.get() == ButtonType.OK) {

							// delete from registerclients.
							RequestObjectClient RejectUser1 = new RequestObjectClient("#REJECT_USER_FROM_REG_CLIENTS_AMAC", // DONE
									String.format("%s#", currRow.getUserName()),
									"DELETE");
							ClientUI.clientController.accept(RejectUser1);

							// delete from users
							RequestObjectClient RejectUser2 = new RequestObjectClient("#REJECT_USER_FROM_USERS_AMAC", // DONE
									String.format("%s#", currRow.getID()), "DELETE");
							ClientUI.clientController.accept(RejectUser2);

							// delete from registrationFormTable
							RequestObjectClient RejectUser3 = new RequestObjectClient("#REJECT_USER_FROM_REG_FORM_AMAC", // DONE
									String.format("%s#", currRow.getID()),
									"DELETE");
							ClientUI.clientController.accept(RejectUser3);
						}
					} else if (currRow.getRegistrationType().equals("Registered To Subscriber")) {

						// change his status back to registered
						RequestObjectClient ApproveUsertoClients = new RequestObjectClient("#REJECT_APPROVED_USER_AMAC", // DONE
								String.format(
										"%s#",
										currRow.getUserName()),
								"PUT");
						ClientUI.clientController.accept(ApproveUsertoClients);

						// delete user from registrationformntable
						RequestObjectClient RejectUser = new RequestObjectClient("#REJECT_USER_FROM_REG_FORM_AMAC", // DONE
								String.format("%s#", currRow.getUserName()),
								"DELETE");
						ClientUI.clientController.accept(RejectUser);

					}

				}

				if (currRow.status.getValue().equals("Approve")) {
					if (currRow.getRegistrationType().equals("Registered")) {
						RequestObjectClient ApproveUsertoClients = new RequestObjectClient("#APPROVE_BASIC_USER_AMC",
								String.format(
										"%s#%s#",
										currRow.getUserName(), currRow.getCreditCard()),
								"PUT");
						ClientUI.clientController.accept(ApproveUsertoClients);



						RequestObjectClient RemoveAfterApproval = new RequestObjectClient("#REJECT_USER_FROM_REG_FORM_AMAC",
								String.format("%s#", currRow.getID()),
								"DELETE");
						ClientUI.clientController.accept(RemoveAfterApproval);

					}

					if (currRow.getRegistrationType().equals("Subscriber")
							|| currRow.getRegistrationType().equals("Registered To Subscriber")) {
						// get maximum subscriber number to create new one

						RequestObjectClient getSubNum = new RequestObjectClient("#GET_MAX_SUB_NUM", 
								String.format(""), "GET");
						ClientUI.clientController.accept(getSubNum);


						RequestObjectClient ApproveUsertoClients = new RequestObjectClient("#APPROVE_SUBSCRIBED_USER_AMAC", 
								String.format(
										"%s#%d#",
										currRow.getUserName(), subNums.peek() + 1),
								"PUT");
						ClientUI.clientController.accept(ApproveUsertoClients);

						RequestObjectClient RemoveAfterApproval = new RequestObjectClient("#REJECT_USER_FROM_REG_FORM_AMAC", 
								String.format("%s#", currRow.getID()),
								"DELETE");
						ClientUI.clientController.accept(RemoveAfterApproval);
					}
				}
			}
		}
		userRows.clear();
		RequestObjectClient requestUsersFromImportTable = new RequestObjectClient("#GET_USERS_AMAC", 
				String.format("%s#",
						ClientUI.clientController.getUser().getArea()),
				"GET");
		ClientUI.clientController.accept(requestUsersFromImportTable);
		
		for (ImportedUser ur : userRows) {
			ComboBox<String> combo = new ComboBox<>();
			combo.setPromptText("Choose Approve / Reject");
			ObservableList<String> info = FXCollections.observableArrayList("Approve", "Reject");
			combo.setItems(info);
			ur.setStatus(combo);
		}

		ObservableList<ImportedUser> info = FXCollections.observableArrayList(userRows);
		ApproveTable.setItems(info);

		Alert information = new Alert(AlertType.INFORMATION);
		information.setContentText("changes have been successfully saved!");
		information.setTitle("Success");
		information.showAndWait();
	}

	
	@FXML
	void closeWindow(ActionEvent event) {
		if (ClientUI.clientController.getUser().getOnlineStatus() == null) {
			System.out.println("Not updated");
		}
		if (ClientUI.clientController.getUser().getOnlineStatus().equals("Online")) {
			RequestObjectClient request = new RequestObjectClient("#USER_UPDATE_STATUS",
					String.format("%s#",
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
			case "#GET_USERS_AMAC":
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
						String userTypeRequest = (String) values[8];
						boolean isSentToManager = (boolean) values[9];
						String cardNumber = (String) values[10];
						User user = new User(firstName, lastName, telephone, Email, ID, userName, userPassword, area);
						ImportedUser impoUser = new ImportedUser(user, area, isSentToManager, userTypeRequest, null,
								cardNumber);
						userRows.add(impoUser);
					}
				}
				break;
			case "#GET_MAX_SUB_NUM":
				if (serverResponse.Responsedata.size() != 0) {
					for (int i = 0; i < serverResponse.Responsedata.size(); i++) {
						Object[] values = (Object[]) serverResponse.Responsedata.get(i);
						if (values[0] != null) {
							int subNum = (int) values[0];
							subNums.add(subNum);
						}
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

		RequestObjectClient requestUsersFromImportTable = new RequestObjectClient("#GET_USERS_AMAC", 
				String.format("%s#",
						ClientUI.clientController.getUser().getArea()),
				"GET");
		ClientUI.clientController.accept(requestUsersFromImportTable);
		for (ImportedUser ur : userRows) {
			ComboBox<String> combo = new ComboBox<>();
			combo.setPromptText("Choose Approve / Reject");
			ObservableList<String> info = FXCollections.observableArrayList("Approve", "Reject");
			combo.setItems(info);
			ur.setStatus(combo);
		}
		IDColumn.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));
		NameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
		RegistrationTypeColumn.setCellValueFactory(new PropertyValueFactory<ImportedUser, String>("registrationType"));
		StatusColumn.setCellValueFactory(new PropertyValueFactory<ImportedUser, ComboBox<String>>("status"));

		ObservableList<ImportedUser> info = FXCollections.observableArrayList(userRows);
		ApproveTable.setItems(info);

	}

}
