package common;

import ocsf.server.ConnectionToClient;

public interface MessageHandler {
	void handle(Object msg, ConnectionToClient client);
}
