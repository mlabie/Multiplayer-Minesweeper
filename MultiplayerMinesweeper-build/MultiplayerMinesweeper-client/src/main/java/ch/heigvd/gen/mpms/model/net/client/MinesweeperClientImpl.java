package ch.heigvd.gen.mpms.model.net.client;

import java.io.*;
import java.net.Socket;

public class MinesweeperClientImpl /*implements IMinesweeperClient*/{
	protected Socket clientSocket;
	protected PrintWriter out;
	protected BufferedReader in;
	protected boolean connected = false;

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

	public void joinLobby(){

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
