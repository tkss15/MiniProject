package gui;

import java.net.URL;
import java.util.ResourceBundle;

import Server.ServerUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class ServerInterfaceController implements Initializable {
	private static String GUI_DIR = "/gui/";
	@FXML
	private Button connectButton;

	@FXML
    private TableView<?> connectedClientsTable;
	@FXML
	private Button CloseButton;

	@FXML
	private Button dissconnectButton;

	@FXML
	private TextArea textConsole;

	@FXML
	private TextField textboxDBName;
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

	public PasswordField getTextPasswordF() {
		return textPasswordF;
	}

	public void setTextPasswordF(PasswordField textPasswordF) {
		this.textPasswordF = textPasswordF;
	}

	public ServerInterfaceController() {
	}

	@FXML
	public void ConnectToDB(ActionEvent event) {
		System.out.println("Controller " + this);
		ServerUI.runServer("5555");

	}
//    public void initialize() {
////    	textConsole.setText("");
////    	dissconnectButton.setDisable(true);
//    }

	@FXML
	public void disconnectFromDB() {
		ServerUI.StopServer("5555");
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

	}

}
