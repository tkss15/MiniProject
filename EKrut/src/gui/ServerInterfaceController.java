package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import server.ServerUI;


public class ServerInterfaceController {
	private static String GUI_DIR = "/gui/";
	@FXML
	private Button connectButton;
	
	@FXML
	private Button dissconnectButton;
	
	public void ConnectToDB() {
		
	}
	
	public void DissconnectFromDB() {
		
	}
	
	public void start(Stage primaryStage) throws IOException 
	{
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerInterface.fxml"));
		
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource(GUI_DIR + "/css/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Server UI");
		primaryStage.setScene(scene);
		
		primaryStage.show();	
	}
	 
	public void writeToConsole() {}
	
}
