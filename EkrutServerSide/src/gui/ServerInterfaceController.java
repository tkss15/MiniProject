package gui;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

import Server.ServerUI;
import common.ClientConnection;
import common.IController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ServerInterfaceController implements Initializable, IController {
	ObservableList<ClientConnection> colums = FXCollections.<ClientConnection>observableArrayList();
	@FXML
	private TableView<ClientConnection> connectedClientsTable = new TableView<ClientConnection>();
	@FXML
	private TableColumn<ClientConnection, String> clientStatus, clientHost, clientIP;
	@FXML
	private Button connectButton, CloseButton, dissconnectButton;
	@FXML
	private Text missingTxt;
	@FXML
	private TextField textboxPort, textboxDBUserName, textboxDBName, textboxIP, textboxSmsAuth, textBoxSmsKey;
	@FXML
	private TextArea textConsole;
	@FXML
	private CheckBox SMSCheckbox;
	@FXML
	private VBox SMSArea;
	@FXML
	private PasswordField textPasswordF;
	@FXML
	private ImageView ConnectLogo;
	@FXML
	private Label labelReport;
	@FXML
	private Button importBTN;

	// changes
	private LinkedHashMap<String, String> txtToTables;

	public ServerInterfaceController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// changes
		txtToTables = new LinkedHashMap<>();
		
		//changes 
		importBTN.setVisible(false);

		clientStatus.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("Status"));
		clientHost.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("Host"));
		clientIP.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("IP"));

		dissconnectButton.setDisable(true);

		missingTxt.setVisible(false);
		SMSArea.setVisible(false);

		textConsole.setEditable(false);
		connectedClientsTable.setEditable(false);

		SMSCheckbox.setOnAction(event -> {
			if (SMSCheckbox.isSelected()) {
				SMSArea.setVisible(true);
			} else {
				SMSArea.setVisible(false);
			}

		});

	}

	public void showConnectedClients(ClientConnection connectedClient) {
		this.connectedClientsTable.getItems().add(connectedClient);
		this.connectedClientsTable.refresh();
	}

	@FXML
	public void ConnectToDB(ActionEvent event) {
		String port = textboxPort.getText();
		ArrayList<String> serverConfing = new ArrayList<String>();

		labelReport.setText(CalculateTime() + " Days");
		serverConfing.add(textboxIP.getText());// 0
		serverConfing.add(textboxPort.getText());// 1
		serverConfing.add(textboxDBName.getText());// 2
		serverConfing.add(textboxDBUserName.getText());// 3
		serverConfing.add(textPasswordF.getText());// 4
		if (SMSCheckbox.isSelected()) {
			serverConfing.add(textboxSmsAuth.getText());// 5
			serverConfing.add(textBoxSmsKey.getText());// 6
		}
		boolean MissingText = false;
		for (String setting : serverConfing) {
			if (MissingText = setting.isEmpty())
				break;
		}
		try {
			ServerUI.runServer(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
		ServerUI.sv.accept(serverConfing);
		missingTxt.setVisible(MissingText);
		importBTN.setVisible(true);
	}

	@FXML
	public void disconnectFromDB() {
		/*
		 * UI: after client clicks on the 'x' button at the top of the page function
		 * force quit of the server-user and closes the server connection.
		 */
		importBTN.setVisible(false);
		connectedClientsTable.getItems().clear();
		ServerUI.sv.accept("#close");
	}

	@FXML
	void closeWindow(ActionEvent event) {
		/*
		 * UI: after client clicks on the 'x' button at the top of the page function
		 * force quit of the server-user and closes the server connection.
		 */
		System.exit(0);
	}

	@FXML
	void importUsers(ActionEvent event) {
		// import from users
		// import from employees
		// import from registeredclients
		
		txtToTables.put("employeestest.csv", "employeestest");
//		txtToTables.put("users.txt", "userstest");
//		txtToTables.put("registeredClientsTXT.txt", "registerclientstest");

		ServerUI.sv.accept(txtToTables);
//			stmt.executeUpdate("load data local infile \"users.txt\" into table users");
//			stmt.executeUpdate("load data local infile \"employees.txt\" into table employees");
//			stmt.executeUpdate("load data local infile \"registeredClientsTXT.txt\" into table registerclients");

	}


	/***
	 * @param message function demonstrating the server-user console. function
	 *                appending the @message and a new line to the text field
	 */
	public void writeToConsole(String message) {
		this.textConsole.appendText(message + "\n");
	}

	/*
	 * 
	 * Getters and Setters for Components in Controller.
	 */
	public ImageView getConnectLogo() {
		return ConnectLogo;
	}

	public void setConnectLogo(ImageView connectLogo) {
		ConnectLogo = connectLogo;
	}

	public Button getConnectButton() {
		return connectButton;
	}

	public void setConnectButton(Button connectButton) {
		this.connectButton = connectButton;
	}

	public TableView<ClientConnection> getTable() {
		return connectedClientsTable;
	}

	public Button getDissconnectButton() {
		return dissconnectButton;
	}

	public void setdisconnectButton(Button dissconnectButton) {
		this.dissconnectButton = dissconnectButton;
	}

	public TextField getTextboxDBName() {
		return textboxDBName;
	}

	public void setTextboxDBName(TextField textboxDBName) {
		this.textboxDBName = textboxDBName;
	}

	public TextField getTextboxDBUserName() {
		return textboxDBUserName;
	}

	public void setTextboxDBUserName(TextField textboxDBUserName) {
		this.textboxDBUserName = textboxDBUserName;
	}

	public String portStr() {
		return textboxPort.getText();
	}

	public PasswordField getTextPasswordF() {
		return textPasswordF;
	}

	public void setTextPasswordF(PasswordField textPasswordF) {
		this.textPasswordF = textPasswordF;
	}

	private float CalculateTime() {
		Calendar CalenderTime = Calendar.getInstance();
		SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
		String timeStamp = simpleFormat.format(CalenderTime.getTime());
		CalenderTime.add(Calendar.MONTH, 1);
		String MonthOnly = "01/" + (new SimpleDateFormat("MM/yyyy")).format(CalenderTime.getTime());

		try {
			Date currentDate = simpleFormat.parse(timeStamp);
			Date nextMonthDate = simpleFormat.parse(MonthOnly);
			long diff = nextMonthDate.getTime() - currentDate.getTime();
			float days = (diff / (1000 * 60 * 60 * 24));
			return days;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void updatedata(Object data) {
		Platform.runLater(() -> {
			if (data instanceof ClientConnection) {
				ClientConnection clientConnectionMessage = (ClientConnection) data;
				if (colums.contains(clientConnectionMessage)) {
					colums.remove(clientConnectionMessage);
				}
				colums.add(clientConnectionMessage);
				connectedClientsTable.setItems(colums);
				connectedClientsTable.refresh();
			} else if (data instanceof String) {
				String message = (String) data;
				switch (message) {
				case "#SetButtonsOff": {
					ConnectLogo.setVisible(true);
					connectButton.setDisable(false);
					dissconnectButton.setDisable(true);
					break;
				}
				case "#SetButtonsOn": {
					ConnectLogo.setVisible(false);
					connectButton.setDisable(true);
					dissconnectButton.setDisable(false);
					break;
				}
				default:
					writeToConsole(message);
					break;
				}

			}

		});
	}
}
