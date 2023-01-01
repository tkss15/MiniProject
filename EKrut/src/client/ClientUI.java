package client;

import gui.ClientInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientUI extends Application {

	public static ClientController clientController;
	private ClientInterfaceController clientInterfaceController;
	
	public static void main(String[] args) {
		launch(args);
	}	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		clientController = new ClientController("localhost",5555);
		
		clientInterfaceController = new ClientInterfaceController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ClientInterface.fxml"));
        Parent root = loader.load();
        clientInterfaceController = (ClientInterfaceController) loader.getController();
        Scene scene = new Scene(root);
        setAppIcon(primaryStage);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show(); 
	}
	private void setAppIcon(Stage primaryStage) {
		primaryStage.setTitle("EKrut System");
        primaryStage.getIcons().add(new Image("\\gui\\pictures\\LogoEKRUT.png"));
	}
	
}
