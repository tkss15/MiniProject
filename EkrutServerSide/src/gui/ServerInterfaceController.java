package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Server.ServerUI;
import common.ClientConnection;
import common.IController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ServerInterfaceController implements Initializable, IController 
{
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
	private TextField textboxPort,textboxDBUserName, textboxDBName, textboxIP;
	@FXML
	private TextArea textConsole;
	@FXML
	private PasswordField textPasswordF;
	@FXML
	private ImageView ConnectLogo;
	
	public ServerInterfaceController() {}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		clientStatus.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("Status"));
		clientHost.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("Host"));
		clientIP.setCellValueFactory(new PropertyValueFactory<ClientConnection, String>("IP"));
		
		dissconnectButton.setDisable(true);
		
		missingTxt.setVisible(false);
		
		textConsole.setEditable(false);
		connectedClientsTable.setEditable(false);

	}
	
	public void showConnectedClients(ClientConnection connectedClient) 
	{
		this.connectedClientsTable.getItems().add(connectedClient);
		this.connectedClientsTable.refresh();
	}

	@FXML
	public void ConnectToDB(ActionEvent event) 
	{
		
		String port = textboxPort.getText();
		ArrayList<String> serverConfing = new ArrayList<String>();
		
		serverConfing.add(textboxIP.getText());//0
		serverConfing.add(textboxPort.getText());//1
		serverConfing.add(textboxDBName.getText());//2
		serverConfing.add(textboxDBUserName.getText());//3
		serverConfing.add(textPasswordF.getText());//4
		
		boolean MissingText = false;
		for(String setting : serverConfing)
		{
			if(MissingText = setting.isEmpty())
				break;
		}
		ServerUI.runServer(port);
		ServerUI.sv.accept(serverConfing);
		missingTxt.setVisible(MissingText);
	}

	@FXML
	public void disconnectFromDB() 
	{
		/*
		 * UI: after client clicks on the 'x' button at the top of the page
		 * function force quit of the server-user and closes the server connection.
		 * */
		connectedClientsTable.getItems().clear();
		ServerUI.sv.accept("#close");
	}
	@FXML
	void closeWindow(ActionEvent event) 
	{
		/*
		 * UI: after client clicks on the 'x' button at the top of the page
		 * function force quit of the server-user and closes the server connection.
		 * */
		System.exit(0);
	} 
	/***
	 * @param message
	 * function demonstrating the server-user console. function appending the @message and a new line to the text field 
	 */
	public void writeToConsole(String message) 
	{
		this.textConsole.appendText(message + "\n");
	}
	
	/*
	 * 
	 * Getters and Setters for Components in Controller.
	 * */
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
//	serverInterface.getConnectLogo().setVisible(!isConnected);
//	serverInterface.getConnectButton().setDisable(isConnected);
//	serverInterface.getDissconnectButton().setDisable(!isConnected);
	@Override
	public void updatedata(Object data) {
		if(data instanceof ClientConnection)
		{
			ClientConnection clientConnectionMessage = (ClientConnection)data;
			if(colums.contains(clientConnectionMessage))
			{
				colums.remove(clientConnectionMessage);
			}
			colums.add(clientConnectionMessage);
			connectedClientsTable.setItems(colums);
			connectedClientsTable.refresh();
		}
		else if (data instanceof String)
		{
			String message = (String)data;
			switch(message)
			{
			case"#SetButtonsOff":
			{
				ConnectLogo.setVisible(true);
				connectButton.setDisable(false);
				dissconnectButton.setDisable(true);
				break;
			}
			case"#SetButtonsOn":
			{
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
		
	}
}
