package gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Database.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ServerInterfaceController implements Initializable {
	private static String GUI_DIR = "/gui/";

	@FXML
	private TextArea textConsole;

	@FXML
	private Button connectButton;

	@FXML
	private Button dissconnectButton;

	public void ConnectToDB() {
		DBConnect dbConnector = new DBConnect();
		dbConnector.connectToDB();
	}

	public void DissconnectFromDB() {

	}

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerInterface.fxml"));

		Scene scene = new Scene(root);
		// scene.getStylesheets().add(getClass().getResource(GUI_DIR +
		// "/css/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Server UI");
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	public void writeToConsole(String msg) {
		textConsole.appendText(msg+"\n");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textConsole = new TextArea();
		
	}

}
