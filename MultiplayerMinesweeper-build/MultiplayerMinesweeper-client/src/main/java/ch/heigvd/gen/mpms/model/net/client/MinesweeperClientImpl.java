package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.net.Protocol.MinesweeperProtocol;

import java.io.*;
import java.net.Socket;

public class MinesweeperClientImpl /*implements IMinesweeperClient*/{
	protected Socket clientSocket;
	protected PrintWriter out;
	protected BufferedReader in;
	protected String serverResponse;
	protected boolean connected = false;
	protected boolean firstRequest = true;

	protected void manageResponse(){

	}

	//@Override
	public void connect(String addressServer, int port){
		try {
			clientSocket = new Socket(addressServer, port);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream());
			connected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//@Override
	public void disconnect() {
		connected = false;
		cleanup();
	}


	//@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void createLobby(String name) {
		try {
			checkFirstRequest();
			out.println(MinesweeperProtocol.CMD_CREATE_LOBBY + name);
			out.flush();

			serverResponse = in.readLine();
			System.out.println("reponse : " + serverResponse);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void joinLobby(String playerName, String lobbyName) {
		try {
			checkFirstRequest();

			out.println(MinesweeperProtocol.CMD_JOIN_LOBBY + playerName + " " + lobbyName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void quitLobby() {

	}

	@Override
	public void sweep(int x, int y) {

	}

	@Override
	public void quitGame() {

	}

	void checkFirstRequest() throws IOException {
		if(firstRequest) {
			while ((serverResponse = in.readLine()).isEmpty()) ;
			System.out.println("first response : " + serverResponse);
			firstRequest = false;
		}
	}

	public void cleanup(){
		try {
			if (in != null) {
				in.close();
			}

			if (out != null)
				out.close();

			if (clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
