package common;


import java.util.Objects;

import ocsf.server.ConnectionToClient;

/**
 * A function that connects the client to the server and 
 * through it you can get or set the: Host, IP-Address and Status.
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
	public ClientConnection(ConnectionToClient client, boolean setStatus) {
		this(client);
		Status = setStatus == true ? "Connected" : "Disconnected";
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
	
	@Override
	public int hashCode() {
		return Objects.hash(Host, IP);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientConnection other = (ClientConnection) obj;
		return Objects.equals(Host, other.Host) && Objects.equals(IP, other.IP);
	}
}
