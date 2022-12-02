package server;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerSide extends AbstractServer
{
	final public static int DEFAULT_PORT = 5555;
	
	public ServerSide(int port) 
	{
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// TODO Auto-generated method stub
		
	}
	protected void serverStarted()
	{
		System.out.println("Server started at port 5555" );
	}
	
	protected void serverStopped()
	{
		System.out.println("Server Stopped" );
	}

}
