package Server;

import java.io.IOException;

import Database.DBConnect;
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
	public static ServerInterfaceController serverC;
	private static EchoServer sv;
	public static void main( String args[] ) throws Exception
	{   
		 launch(args);
	} // end main
	private double offset_x;
	private double offset_y;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerInterface.fxml"));
        Parent root = loader.load();
        serverC = (ServerInterfaceController) loader.getController();
        Scene scene = new Scene(root);
        scene.setOnMousePressed(event -> {
            offset_x = event.getSceneX();
            offset_y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - offset_x);
            primaryStage.setY(event.getScreenY() - offset_y);
        });
        setAppIcon(primaryStage);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

	}

	private void setAppIcon(Stage primaryStage) {
		primaryStage.setTitle("EKrut System");
        primaryStage.getIcons().add(new Image("\\gui\\pictures\\LogoEKRUT.png"));
	}
	public static void StopServer(String p)
	{
		if(sv.isListening())
		{
			try {
				sv.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	        	serverC.writeToConsole("ERROR - Could not connect!");
	        }
	    	
		        sv = new EchoServer(port, serverC);		    
		        //DBConnect mySql = new DBConnect(aFrame);
	        try 
	        {
	          sv.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	        	serverC.writeToConsole("ERROR - Could not listen for clients!");
	        }
	}
	

}