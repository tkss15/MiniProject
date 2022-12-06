package client;


import ocsf.server.ConnectionToClient;

/**
 * A function that connects the client to the server and 
 * through it you can get or set the: hostName, ipAddress and status.
 *
 */
public class ClientConnection {
	private String Host;
	private String IP;
	private String Status;

	public ClientConnection(ConnectionToClient client) {
		Status = client.isAlive() == true ? "Connected" : "Disconnected";
		IP = client.getInetAddress().getHostAddress();
		Host = client.getInetAddress().getHostName();
	}

	public String getHost() {
		return Host;
	}

	public void setHost(String host) {
		Host = host;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}
