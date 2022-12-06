package Server;

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
	public static ServerInterfaceController serverInterface;
	public static void main( String args[] ) throws Exception
	{   
		 launch(args);
	}
	
	private double offset_x;
	private double offset_y;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
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
	
	public static void runServer(String p)
	{
		 int port = 5555; //Port to listen on

	        try
	        {
	        	port = Integer.parseInt(p); //Set port to 5555
	          
	        }
	        catch(Throwable t)
	        {
	        	System.out.println("ERROR - Could not connect!");
	        	//serverC.writeToConsole("ERROR - Could not connect!");
	        }
	    	
	        	sv = new ServerConsole(port, serverInterface);		    
		        //DBConnect mySql = new DBConnect(aFrame);
	        try 
	        {
	          sv.server.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	        	System.out.println("k");
	        	System.out.println("ERROR - Could not listen for clients!");
	        	//serverC.writeToConsole("ERROR - Could not listen for clients!");
	        }
	}
	
}