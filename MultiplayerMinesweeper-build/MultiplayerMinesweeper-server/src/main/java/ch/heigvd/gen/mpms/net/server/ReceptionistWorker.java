package ch.heigvd.gen.mpms.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.heigvd.gen.mpms.net.Protocol.MinesweeperProtocol;

/**
 * cf https://wasadigi.gitbooks.io/res-heigvd-network-programming-application-protoc/content/tcp_programming.html
 */
public class ReceptionistWorker implements Runnable {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());

    private int port;


    public ReceptionistWorker(){
        this(MinesweeperProtocol.DEFAULT_PORT);
    }


    public ReceptionistWorker(int port){
        this.port = port;
    }


    //@Override
    public void run() {
        ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        while (true) {
            LOG.info("Waiting (blocking) for a new client...");
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServantWorker(clientSocket)).start();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }

    }
}
