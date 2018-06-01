package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.GameComponent.Configuration;
import ch.heigvd.gen.mpms.GameComponent.Player;
import ch.heigvd.gen.mpms.game.BoardGame;
import ch.heigvd.gen.mpms.game.Square;
import ch.heigvd.gen.mpms.model.net.Protocol.MinesweeperProtocol;
import ch.heigvd.gen.mpms.model.net.server.ReceptionistWorker;
import ch.heigvd.gen.mpms.model.net.server.ServantWorker;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class MinesweeperServer {
    public static void main(String args[]){
//        int port;
//
//        if(args.length == 0) {
//
//            port = MinesweeperProtocol.DEFAULT_PORT;
//
//        } else if(args.length == 1) {
//
//            port = Integer.parseInt(args[0]);
//
//            if(port > 6000 || port < 1000){
//                System.out.println("The port : " + port + " is not an available one.");
//                return;
//            }
//
//        } else {
//            System.out.println("Too many arguments.");
//            return;
//        }
//
//        new Thread(new ReceptionistWorker(port)).start();

		Configuration configuration = new Configuration("hello");
		configuration.setScore(Configuration.ScoreMode.STANDARD);
		configuration.setBonus(true);
		configuration.setMineProportion(10);
		configuration.setHeight(16);
		configuration.setWidth(16);
		BoardGame b = new BoardGame(configuration);
		System.out.println("Board state 1: \n" + b);
		Vector<Square> tab = new Vector<>();
		b.sweep(10, 5, new Player("hello"), tab);
		System.out.print("Square swept: " );
		for(Square s : tab){
			System.out.print(s.getValue() + ", ");
		}
		System.out.println();
		b.sweep(10,15, new Player("hello"), tab);
		System.out.println("Board state 2: \n" + b);

    }

}
