package client;

import java.io.IOException;

public class ClientController {
	  public static int DEFAULT_PORT ;
	  ChatClient client;
	  
	public ClientController(String host, int port) {
		try 
	    {
	      client= new ChatClient(host, port);
	    } 
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!"+ " Terminating client.");
	      System.exit(1);
	    }
	}
	
	 public void accept(Object msg) 
	  {
		  client.handleMessageFromClientUI(msg);
	  }
	

}
