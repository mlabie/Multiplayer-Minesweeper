package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.net.client.MinesweeperClientImpl;

public class MinesweeperClient {
    public static void main(String args[]){

    	System.out.println("Hello Client !");

    	MinesweeperClientImpl client = new MinesweeperClientImpl();
    	client.connect("10.192.93.119", 1001);
    	client.createLobby("HelloWorld");
		client.createLobby("HelloWorld");
    	client.disconnect();
    }
}
