package Server;

import java.util.ArrayList;

import common.SceneManager;
import gui.ServerInterfaceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ServerUI extends Application
{
	final public static int DEFAULT_PORT = 5555;	
	public static ServerConsole sv;
	private static ServerInterfaceController serverInterface;
	public static void main( String args[] ) throws Exception
	{   
		 launch(args);
	}
	
	private double offset_x;
	private double offset_y;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
//		  SceneManager sceneManager = new SceneManager();
//		  sceneManager.ShowScene("/gui/LoginInterface.fxml");
//		  sercv
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerInterface.fxml"));
        Parent root = loader.load();
        serverInterface = (ServerInterfaceController) loader.getController();
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
	
	private void setAppIcon(Stage primaryStage) 
	{
		primaryStage.setTitle("EKrut System");
        primaryStage.getIcons().add(new Image("\\gui\\pictures\\serverIcon.png"));
	}
	
	public static void runServer(String strPort)
	{
		 int port = 0; //Port to listen on

	        try
	        {
	        	port = Integer.parseInt(strPort); //Set port to 5555
	          
	        }
	        catch(Throwable t)
	        {
	        	System.out.println("ERROR - Could not connect!1");
	        }
	    	
	        	sv = new ServerConsole(port,serverInterface);		    
	        try 
	        {
	          sv.server.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	        	System.out.println("ERROR - Could not listen for clients!");
	        	//serverC.writeToConsole("ERROR - Could not listen for clients!");
	        }
	}
	
}