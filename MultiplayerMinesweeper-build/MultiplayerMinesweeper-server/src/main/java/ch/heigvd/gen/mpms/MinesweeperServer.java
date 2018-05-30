package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.game.BoardGame;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.model.net.server.ReceptionistWorker;

public class MinesweeperServer {
    public static void main(String args[]){
        int port;

        if(args.length == 0) {

            port = MinesweeperProtocol.DEFAULT_PORT;

        } else if(args.length == 1) {

            port = Integer.parseInt(args[0]);

            if(port > 6000 || port < 1000){
                System.out.println("The port : " + port + " is not an available one.");
                return;
            }

        } else {
            System.out.println("Too many arguments.");
            return;
        }

        new Thread(new ReceptionistWorker(port)).start();


		BoardGame b = new BoardGame(10, 20, true);
		System.out.println("Board state 1: \n" + b);
		b.sweep(5, 5);
		System.out.println("Board state 2: \n" + b);
		b.sweep(1,6);
		System.out.println("Board state 2: \n" + b);

    }

}
