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

public class ServerUI extends Application 
{
	final public static int DEFAULT_PORT = 5555;	
	public static ServerInterfaceController serverC;
	private static ServerSide sv;
	public static void main( String args[] ) throws Exception
	{   
		 launch(args);
	} // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerInterface.fxml"));
        Parent root = loader.load();
        serverC = (ServerInterfaceController) loader.getController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

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
			sv.stopListening();
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
	        	System.out.println("ERROR - Could not connect!");
	        }
	    	
		        sv = new ServerSide(port, serverC);		    
		        //DBConnect mySql = new DBConnect(aFrame);
	        try 
	        {
	          sv.listen(); //Start listening for connections
	        } 
	        catch (Exception ex) 
	        {
	          System.out.println("ERROR - Could not listen for clients!");
	        }
	}
	

}