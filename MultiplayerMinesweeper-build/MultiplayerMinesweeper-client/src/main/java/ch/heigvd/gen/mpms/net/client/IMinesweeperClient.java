package ch.heigvd.gen.mpms.net.client;

public interface IMinesweeperClient {

	void connect(String addressServer, int port);

	void disconnect();

	boolean isConnected();

	void createLobby(String name);

	void joinLobby(String playerName, String lobbyName);

	void quitLobby();

	void sweep(int x, int y);

	void quitGame();
}
