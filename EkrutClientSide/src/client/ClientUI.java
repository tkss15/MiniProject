package client;

import gui.ClientInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientUI extends Application 
{

	public static ClientConsole clientController;
	private double offset_x;
	private double offset_y;
	
	public static void main(String[] args) 
	{
		launch(args);
	}	
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		ShowScence(primaryStage, "/gui/LogInServer.fxml");		
	}
	
	public static void ConnectToServer(String p)
	{
		int port = 5555; //Port to listen on
        clientController = new ClientConsole(p,port);		    
	}
	
	public void ShowScence(Stage primaryStage, String URL) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(URL));
	    Parent root = loader.load();
			
	    Scene scene = new Scene(root);
	    scene.setOnMousePressed(event -> {
	        offset_x = event.getSceneX();
	        offset_y = event.getSceneY();
	    });
	
	    scene.setOnMouseDragged(event -> {
	        primaryStage.setX(event.getScreenX() - offset_x);
	        primaryStage.setY(event.getScreenY() - offset_y);
	    });
	    
	    //setAppIcon(primaryStage);
	    primaryStage.initStyle(StageStyle.UNDECORATED);
	    primaryStage.setScene(scene);
	    primaryStage.show(); 
	}
	private void setAppIcon(Stage primaryStage) {
		primaryStage.setTitle("EKrut System");
        primaryStage.getIcons().add(new Image("\\gui\\pictures\\LogoEKRUT.png"));
	}
	
}
