package client;

import java.net.URL;

import common.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/***
 * 
 *ClientUI launches the first UI to the client the UI have a button of connect and a text field that 
 *
 */
public class ClientUI extends Application 
{

	public static ClientConsole clientController;
	private double offset_x;
	private double offset_y;
	public static SceneManager sceneManager = new SceneManager();/// an instance of scene manager so it will be easier to switch between UI, and controllers
	
	public static void main(String[] args) 
	{
		launch(args);
	}	
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		ShowScence(primaryStage, "/LogInServer.fxml");		
	}
	
	public static void ConnectToServer(String p)
	{
		int port = 5555; //Port to listen on
        clientController = new ClientConsole(p,port);		    
	}
	
	public void ShowScence(Stage primaryStage, String URL) throws Exception
	{
	    URL url = getClass().getResource(URL);
		//System.out.println(ClientUI.class.getResource("../src/gui/ServerInterface.fxml"));
        //FXMLLoader loader = new FXMLLoader(ServerUI.class.getResource("/src/gui/ServerInterface.fxml"));
		FXMLLoader loader = new FXMLLoader(url);
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
	    
	    primaryStage.initStyle(StageStyle.UNDECORATED);
	    primaryStage.setScene(scene);
	    primaryStage.show(); 
	}
	
}
