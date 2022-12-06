package gui;

import java.net.URL;
import java.util.ResourceBundle;

import Server.ServerUI;
import client.ClientConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ServerInterfaceController implements Initializable {
	private static String GUI_DIR = "/gui/";
	@FXML
	private Button connectButton;

	@FXML
    private TableView<ClientConnection> connectedClientsTable = new TableView<ClientConnection>();
	@FXML
	private TableColumn<ClientConnection, String> clientStatus;

	@FXML
	private TableColumn<ClientConnection, String> clientHost;

	@FXML
	private TableColumn<ClientConnection, String> clientIP;
	@FXML
	private Button CloseButton;

	@FXML
	private Button dissconnectButton;
	
	@FXML
	private Text missingTxt;

	@FXML
	private TextArea textConsole;
	
	@FXML
	private TextField textboxIP;

	@FXML
	private TextField textboxDBName;
	
	@FXML
	private TextField textboxPort;
	
	@FXML
	private TextField textboxDBUserName;
	@FXML
	private PasswordField textPasswordF;
	@FXML
	private ImageView ConnectLogo;

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
	
	public void showConnectedClients(ClientConnection connectedClient) {
		this.connectedClientsTable.getItems().add(connectedClient);
		this.connectedClientsTable.refresh();
	}

	public ServerInterfaceController() {}

	@FXML
	public void ConnectToDB(ActionEvent event) {
		System.out.println("Controller " + this);
		String ipText = textboxIP.getText();
		String portTxt = textboxPort.getText();
		String DBNameTxt = textboxDBName.getText();
		String DBUserNameTxt = textboxDBUserName.getText();
		String DBPasswordTxt = textPasswordF.getText();
		if(ipText.isEmpty() || portTxt.isEmpty() || DBNameTxt.isEmpty() || DBUserNameTxt.isEmpty() || DBPasswordTxt.isEmpty()) {
			missingTxt.setVisible(true);
		}else {
			ServerUI.runServer(portTxt);
			missingTxt.setVisible(false);
		}
	}

	@FXML
	public void disconnectFromDB() {
		ServerUI.sv.accept("#close");
	}

	@FXML
	void closeWindow(ActionEvent event) {
		System.exit(0);
	}

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

	public void writeToConsole(String message) {
		this.textConsole.appendText(message + "\n");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dissconnectButton.setDisable(true);
		missingTxt.setVisible(false);
		textConsole.setEditable(false);
		clientStatus.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("Status"));
		clientHost.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("Host"));
		clientIP.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("IP"));
		connectedClientsTable.setEditable(false);
	}

}
